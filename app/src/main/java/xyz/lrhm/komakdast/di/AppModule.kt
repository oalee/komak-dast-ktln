package xyz.lrhm.komakdast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.createDataStore
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import xyz.lrhm.komakdast.core.data.source.local.db.AppDao
import xyz.lrhm.komakdast.core.data.source.local.db.AppDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
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

    @Singleton
    @Provides
    fun providesMoshi() = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

}