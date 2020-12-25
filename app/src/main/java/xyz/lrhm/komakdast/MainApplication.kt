package xyz.lrhm.komakdast

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.source.PreferenceRepository
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()

        preferenceRepository.loadDataFromIO()

            Timber.d("this is me, ${preferenceRepository.toString()} ${preferenceRepository._prefs?.runCounter}" )
    }
}