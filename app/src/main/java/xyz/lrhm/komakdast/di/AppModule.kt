package xyz.lrhm.komakdast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import xyz.lrhm.komakdast.core.data.source.local.db.AppDao
import xyz.lrhm.komakdast.core.data.source.local.db.AppDatabase
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule  {

//    @Provides
//    @Singleton
//    fun providesContext() = context

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, "localStore.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideUserDao(appDatabase: AppDatabase): AppDao {
        return appDatabase.appDao()
    }

    @Singleton
    @Provides
    fun providesDataStore(@ApplicationContext context: Context): DataStore<Preferences> = context.createDataStore(  "settings")

}