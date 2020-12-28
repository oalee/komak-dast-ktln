package xyz.lrhm.komakdast.core.data.source.local

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package
import xyz.lrhm.komakdast.core.data.source.local.db.AppDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(val appDao: AppDao) {

    suspend fun insertLessons(lessons: List<Lesson>) = withContext(Dispatchers.IO) {
        appDao.insertLessons(lessons)
    }

    suspend fun deleteLessonsForPackage(id: Int) = withContext(Dispatchers.IO) {
        appDao.deleteLessonsForPackage(id)
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


}