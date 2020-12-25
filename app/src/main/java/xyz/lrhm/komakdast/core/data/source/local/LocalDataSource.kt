package xyz.lrhm.komakdast.core.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.source.local.db.AppDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(val appDao: AppDao) {

    suspend fun insertLessons(lessons: List<Lesson>) = withContext(Dispatchers.IO) {
        appDao.insertLessons(lessons)
    }

    suspend fun getAllLessons() = withContext(Dispatchers.IO) {
        return@withContext appDao.getLessons()
    }
}