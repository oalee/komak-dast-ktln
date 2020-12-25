package xyz.lrhm.komakdast.core.data.source.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import xyz.lrhm.komakdast.core.data.model.Lesson

@Dao
interface AppDao {

    @Insert
    fun insertLessons(lessons : List<Lesson>)

    @Query("Select * from Lesson")
    fun getLessons(): List<Lesson>
}