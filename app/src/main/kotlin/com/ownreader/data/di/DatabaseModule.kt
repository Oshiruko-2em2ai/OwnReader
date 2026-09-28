package com.ownreader.data.di

import android.content.Context
import androidx.room.Room
import com.ownreader.data.database.AppDatabase
import com.ownreader.data.database.dao.ComicDao
import com.ownreader.data.database.dao.PageDao
import com.ownreader.data.database.dao.ReadHistoryDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ownreader.db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideComicDao(database: AppDatabase): ComicDao {
        return database.comicDao()
    }

    @Singleton
    @Provides
    fun providePageDao(database: AppDatabase): PageDao {
        return database.pageDao()
    }

    @Singleton
    @Provides
    fun provideReadHistoryDao(database: AppDatabase): ReadHistoryDao {
        return database.readHistoryDao()
    }
}
