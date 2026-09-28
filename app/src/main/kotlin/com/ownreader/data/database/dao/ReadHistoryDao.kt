package com.ownreader.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ownreader.data.model.ReadHistory
import kotlinx.coroutines.flow.Flow

@Dao
interface ReadHistoryDao {
    @Insert
    suspend fun insert(readHistory: ReadHistory)

    @Update
    suspend fun update(readHistory: ReadHistory)

    @Delete
    suspend fun delete(readHistory: ReadHistory)

    @Query("SELECT * FROM read_history WHERE comicId = :comicId")
    fun getByComicId(comicId: Long): Flow<ReadHistory?>

    @Query("SELECT * FROM read_history ORDER BY lastReadAt DESC")
    fun getAllHistory(): Flow<List<ReadHistory>>

    @Query("DELETE FROM read_history")
    suspend fun deleteAll()
}
