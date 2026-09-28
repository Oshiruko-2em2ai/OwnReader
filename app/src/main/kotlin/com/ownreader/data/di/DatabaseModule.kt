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
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "ownreader.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideComicDao(db: AppDatabase): ComicDao = db.comicDao()

    @Provides
    @Singleton
    fun providePageDao(db: AppDatabase): PageDao = db.pageDao()

    @Provides
    @Singleton
    fun provideReadHistoryDao(db: AppDatabase): ReadHistoryDao = db.readHistoryDao()
}
