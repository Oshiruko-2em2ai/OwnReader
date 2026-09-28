package com.ownreader.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ownreader.data.database.converter.DateConverter
import com.ownreader.data.database.dao.ComicDao
import com.ownreader.data.database.dao.PageDao
import com.ownreader.data.database.dao.ReadHistoryDao
import com.ownreader.data.model.Comic
import com.ownreader.data.model.Page
import com.ownreader.data.model.ReadHistory

@Database(
    entities = [
        Comic::class,
        Page::class,
        ReadHistory::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun comicDao(): ComicDao
    abstract fun pageDao(): PageDao
    abstract fun readHistoryDao(): ReadHistoryDao
}
