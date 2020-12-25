package xyz.lrhm.komakdast.core.data.source

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import xyz.lrhm.komakdast.core.data.source.local.prefs.AppPreferences
import xyz.lrhm.komakdast.core.data.source.local.prefs.PreferencesKeys
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceRepository @Inject constructor(private val dataStore: DataStore<Preferences>) {

    private val _livePreferences: MutableLiveData<AppPreferences> = MutableLiveData()
    val livePreferences : LiveData<AppPreferences> = _livePreferences

    val cachedPreferences: AppPreferences by lazy {
        _prefs!!
    }

    private var _prefs: AppPreferences? = null
    private val userPreferencesFlow = dataStore.data.catch { exception ->
        // dataStore.data throws an IOException when an error is encountered when reading data
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map {

      val showCompleted =  it[PreferencesKeys.SHOW_COMPLETED]?: false
      val runCounter =   it[PreferencesKeys.RUN_COUNTER]?: 0

        AppPreferences(runCounter , showCompleted)
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userPreferencesFlow.collect {
                _prefs = it

                CoroutineScope(Dispatchers.Main).launch {
                _livePreferences.value = _prefs
                }

            }


        }
    }


    fun increaseAppCounter(){

        CoroutineScope(Dispatchers.IO).launch {
            dataStore.edit { settings->

                if (_prefs == null)
                    throw IOException("PREFS ARE NULL")

                // save to persistent
                settings[PreferencesKeys.RUN_COUNTER] = _prefs!!.runCounter + 1
                _prefs!!.runCounter = _prefs!!.runCounter+1
                CoroutineScope(Dispatchers.Main).launch {
                    _livePreferences.value = _prefs

                }
            }

        }

    }





}