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
class AppRepository @Inject constructor(@ApplicationContext val context: Context, val localDataSource: LocalDataSource) {

    suspend fun getAllLessons(): List<Lesson> {
        return localDataSource.getAllLessons()
    }

    suspend fun getPackages() = localDataSource.getPackages()

    suspend fun insertPackage(p: Package, lessons: List<Lesson>){

        localDataSource.deleteLessonsForPackage(p.id)

        for (l in lessons)
            l.packageId = p.id
        localDataSource.insertPackage(p)
        localDataSource.insertLessons(lessons)

    }


}