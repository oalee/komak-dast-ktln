package xyz.lrhm.komakdast

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : Application() {
//
//    @Inject
//    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()

    }
}