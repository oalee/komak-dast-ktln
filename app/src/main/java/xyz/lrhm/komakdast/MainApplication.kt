package xyz.lrhm.komakdast

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication : MultiDexApplication() {
//
//    @Inject
//    lateinit var preferenceRepository: PreferenceRepository

    override fun onCreate() {
        Timber.plant(Timber.DebugTree())
        super.onCreate()

    }
}