package xyz.lrhm.komakdast.core.data.source

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package
import xyz.lrhm.komakdast.core.data.source.local.LocalDataSource
import java.io.IOException
import java.io.InputStream
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AppRepository @Inject constructor( val localDataSource: LocalDataSource) {

    var cachedPackages: List<Package>? = null
    var cachedLessons: List<Lesson>? = null

    suspend fun getAllLessons(): List<Lesson> {
        cachedLessons = localDataSource.getAllLessons()

        return cachedLessons!!
    }

    suspend fun getPackages(): List<Package> {

        cachedPackages = localDataSource.getPackages()

        return cachedPackages!!
    }
    suspend fun insertPackage(p: Package, lessons: List<Lesson>){

        localDataSource.deleteLessonsForPackage(p.id)

        for (l in lessons)
            l.packageId = p.id
        localDataSource.insertPackage(p)
        localDataSource.insertLessons(lessons)

    }


}