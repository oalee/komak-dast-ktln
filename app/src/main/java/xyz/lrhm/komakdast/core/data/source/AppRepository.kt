package xyz.lrhm.komakdast.core.data.source

import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.source.local.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepository @Inject constructor(val localDataSource: LocalDataSource) {

    suspend fun getAllLessons(): List<Lesson> {
        return localDataSource.getAllLessons()
    }
}