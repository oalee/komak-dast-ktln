package xyz.lrhm.komakdast.core.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package

@Dao
interface AppDao {

    @Insert
    fun insertPackage(p: Package)

    @Insert
    fun insertLessons(lessons: List<Lesson>)

    @Query("Select * from Lesson")
    fun getLessons(): List<Lesson>

    @Query("Select * from Lesson where Lesson.packageId == :packageId  and Lesson.resolved == 1 order by Lesson.id desc limit 1")
    fun getLastResolvedLesson(packageId: Int): Lesson


    @Query("Select * from Package")
    fun getPackages(): List<Package>

    @Query("Delete from Lesson where packageId == :id")
    fun deleteLessons(id: Int)

    @Query("Delete from Package where id == :id")
    fun deletePackage(id: Int)


    @Update
    fun updateLesson(lesson: Lesson)

}