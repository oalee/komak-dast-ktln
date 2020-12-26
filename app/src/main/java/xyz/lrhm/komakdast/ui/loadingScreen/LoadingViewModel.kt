package xyz.lrhm.komakdast.ui.loadingScreen

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.source.AppRepository
import xyz.lrhm.komakdast.core.data.source.PreferenceRepository
import xyz.lrhm.komakdast.core.util.LocalUtil

class LoadingViewModel @ViewModelInject constructor(
    val appRepository: AppRepository,
    val preferenceRepository: PreferenceRepository,
    val localUtil: LocalUtil
) : ViewModel() {

    val dataStatus = MutableLiveData<DataStatus>()

    init {
        startDataLoad()
    }

    fun startDataLoad() {

        dataStatus.value = DataStatus.Loading

        Timber.d("ok start")


        viewModelScope.launch {

            try {
                localUtil.syncLocalPackagesWithDB()

                val packages = appRepository.getPackages()
                val levels = appRepository.getAllLessons()

                
                dataStatus.value = DataStatus.Loaded

            } catch (
                e: Exception
            ) {
                Timber.d("ok not $e")

                dataStatus.value = DataStatus.Error
            }

        }

    }


    enum class DataStatus {
        Loading, Loaded, Error
    }
}