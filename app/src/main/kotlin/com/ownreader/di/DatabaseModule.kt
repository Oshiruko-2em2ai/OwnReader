package com.ownreader.di

import android.content.Context
import androidx.room.Room
import com.ownreader.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ownreader_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideComicDao(appDatabase: AppDatabase) = appDatabase.comicDao()

    @Provides
    @Singleton
    fun providePageDao(appDatabase: AppDatabase) = appDatabase.pageDao()

    @Provides
    @Singleton
    fun provideReadHistoryDao(appDatabase: AppDatabase) = appDatabase.readHistoryDao()
}
