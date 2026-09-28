package com.ownreader.data.repository

import com.ownreader.data.database.dao.ReadHistoryDao
import com.ownreader.data.model.ReadHistory
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadHistoryRepository @Inject constructor(
    private val readHistoryDao: ReadHistoryDao
) {
    suspend fun insertReadHistory(readHistory: ReadHistory) = readHistoryDao.insert(readHistory)
    suspend fun updateReadHistory(readHistory: ReadHistory) = readHistoryDao.update(readHistory)
    suspend fun deleteReadHistory(readHistory: ReadHistory) = readHistoryDao.delete(readHistory)
    fun getByComicId(comicId: Long): Flow<ReadHistory?> = readHistoryDao.getByComicId(comicId)
    fun getAllHistory(): Flow<List<ReadHistory>> = readHistoryDao.getAllHistory()
    suspend fun deleteAll() = readHistoryDao.deleteAll()
}
