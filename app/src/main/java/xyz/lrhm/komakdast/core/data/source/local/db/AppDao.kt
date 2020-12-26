package xyz.lrhm.komakdast.core.data.source.local.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import xyz.lrhm.komakdast.core.data.model.*

@Dao
interface AppDao {

    @Insert
    fun insertPackage(p: Package)

    @Insert
    fun insertLessons(lessons : List<Lesson>)

    @Query("Select * from Lesson")
    fun getLessons(): List<Lesson>

    @Query("Select * from Package")
    fun getPackages(): List<Package>

    @Query("Delete from Lesson where packageId == :id")
    fun deleteLessonsForPackage(id: Int)

}