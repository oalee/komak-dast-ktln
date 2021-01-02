package xyz.lrhm.komakdast.core.data.source.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import xyz.lrhm.komakdast.core.data.model.Lesson
import xyz.lrhm.komakdast.core.data.model.Package

@Database(entities = [Lesson::class, Package::class], version = 8)
@TypeConverters(TypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao
}
