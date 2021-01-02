package xyz.lrhm.komakdast.core.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package
import xyz.lrhm.komakdast.core.data.source.local.db.AppDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(val appDao: AppDao) {

    suspend fun insertLessons(lessons: List<Lesson>) = withContext(Dispatchers.IO) {
        appDao.insertLessons(lessons)
    }


    suspend fun insertPackage(p: Package) = withContext(Dispatchers.IO) {
        appDao.insertPackage(p)
    }

    suspend fun getPackages() = withContext(Dispatchers.IO) {
        appDao.getPackages()
    }

    suspend fun getAllLessons() = withContext(Dispatchers.IO) {
        appDao.getLessons()
    }

    suspend fun updateLesson(lesson: Lesson) = withContext(Dispatchers.IO) {
        appDao.updateLesson(lesson)
    }

    suspend fun clearDb(pacakgeId: Int) = withContext(Dispatchers.IO) {
        val lessons = appDao.getLastResolvedLesson(pacakgeId)
        Timber.d("last resolved lesson id is ${lessons.id}")
        appDao.deletePackage(pacakgeId)
        appDao.deleteLessons(pacakgeId)

        lessons.id

    }

}