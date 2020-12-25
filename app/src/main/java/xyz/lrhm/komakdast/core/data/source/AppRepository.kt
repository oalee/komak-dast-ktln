package xyz.lrhm.komakdast.core.data.source

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.source.local.LocalDataSource
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepository @Inject constructor(@ApplicationContext val context: Context, val localDataSource: LocalDataSource) {

    suspend fun getAllLessons(): List<Lesson> {
        return localDataSource.getAllLessons()
    }

    fun parseLocalData() {

        try {
            val inputStream= context.assets.open("")
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
           val string = String(buffer)

        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
}