package xyz.lrhm.komakdast.core.util

import android.content.Context
import android.util.DisplayMetrics
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SizeManager @Inject constructor(@ApplicationContext context: Context) {
    var dm: DisplayMetrics =
        context.resources.displayMetrics

    val deviceWidth = dm.widthPixels

    val deviceHeight = dm.heightPixels

}