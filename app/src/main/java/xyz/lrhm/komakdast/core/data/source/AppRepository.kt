package xyz.lrhm.komakdast.core.data.source

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package
import xyz.lrhm.komakdast.core.data.source.local.LocalDataSource
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepository @Inject constructor(val localDataSource: LocalDataSource) {

    var cachedPackages: List<Package>? = null
    var cachedLessons: List<Lesson>? = null

    fun getCachedLessonsForPackageAndPage(packageId: Int, pageId: Int): List<Lesson> {

        var temp = cachedLessons!!.filter { it.packageId == packageId }

        val startIdx = pageId * 16
        val endIdx = (startIdx + 16).coerceAtMost(temp.size)
        Timber.d("the list is $temp")
        Timber.d("end idx is $endIdx and start is $startIdx and size is ${temp.size}")

        return temp.subList(startIdx, endIdx)

    }

    suspend fun getAllLessons(): List<Lesson> {
        cachedLessons = localDataSource.getAllLessons()

        return cachedLessons!!
    }

    suspend fun getPackages(): List<Package> {

        cachedPackages = localDataSource.getPackages()

        return cachedPackages!!
    }

    suspend fun insertPackage(p: Package, lessons: List<Lesson>) {

        localDataSource.deleteLessonsForPackage(p.id)

        localDataSource.insertPackage(p)
        localDataSource.insertLessons(lessons)

    }

    fun isLessonOpen(lesson: Lesson): Boolean {
        if (lesson.id == 1 || lesson.resolved || (cachedLessons?.find { it.id == lesson.id - 1 && it.packageId == lesson.packageId }?.resolved == true))
            return true
        return false
    }

    fun getLesson(key: Int) = cachedLessons!!.find { it.key == key }

    fun getLastLevelIdForPackage(packageId: Int) =
        cachedLessons!!.filter { it.packageId == packageId }.last().id

    fun getPackageSize(packageId: Int) = cachedLessons!!.filter { it.packageId == packageId }.size

    fun resolveLevel(lesson: Lesson) {

        lesson.resolved = true
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.updateLesson(lesson)
        }
        cachedLessons!!.find { it.key == lesson.key }!!.resolved = true

    }

    fun getNextLevel(level: Lesson): Lesson? {

        val nextLesson =
            cachedLessons!!.find { it.id == level.id + 1 && it.packageId == level.packageId }

        return nextLesson
    }

}