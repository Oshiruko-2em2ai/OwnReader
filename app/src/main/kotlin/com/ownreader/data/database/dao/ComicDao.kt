package com.ownreader.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ownreader.data.model.Comic
import kotlinx.coroutines.flow.Flow

@Dao
interface ComicDao {
    @Insert
    suspend fun insert(comic: Comic): Long

    @Update
    suspend fun update(comic: Comic)

    @Delete
    suspend fun delete(comic: Comic)

    @Query("SELECT * FROM comics WHERE id = :id")
    fun getById(id: Long): Flow<Comic?>

    @Query("SELECT * FROM comics WHERE folderPath = :folderPath LIMIT 1")
    fun getByFolderPath(folderPath: String): Flow<Comic?>

    @Query("SELECT * FROM comics ORDER BY title ASC")
    fun getAllComics(): Flow<List<Comic>>

    @Query("SELECT * FROM comics WHERE title LIKE '%' || :query || '%'")
    fun searchByTitle(query: String): Flow<List<Comic>>

    @Query("DELETE FROM comics")
    suspend fun deleteAll()
}
