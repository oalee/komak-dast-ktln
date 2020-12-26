package xyz.lrhm.komakdast.core.data.source.local.prefs

data class AppPreferences(
    var runCounter: Int,
    val localDataImported: Boolean = false,
    var showedIntro: Boolean = false
) {
}