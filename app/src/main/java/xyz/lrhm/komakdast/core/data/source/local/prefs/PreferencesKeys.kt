package xyz.lrhm.komakdast.core.data.source.local.prefs

import androidx.datastore.preferences.core.preferencesKey

object PreferencesKeys {
    val SHOW_COMPLETED = preferencesKey<Boolean>("show_completed")
    val SHOWED_INTRO = preferencesKey<Boolean>("showed_intro")

    val RUN_COUNTER = preferencesKey<Int>("run_counter")

}