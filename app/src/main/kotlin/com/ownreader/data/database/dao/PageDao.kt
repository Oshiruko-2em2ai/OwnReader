package com.ownreader.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ownreader.data.model.Page
import kotlinx.coroutines.flow.Flow

@Dao
interface PageDao {
    @Insert
    suspend fun insert(page: Page)

    @Update
    suspend fun update(page: Page)

    @Delete
    suspend fun delete(page: Page)

    @Query("SELECT * FROM pages WHERE comicId = :comicId ORDER BY pageNumber ASC")
    fun getPagesByComicId(comicId: Long): Flow<List<Page>>

    @Query("SELECT * FROM pages WHERE id = :id")
    fun getById(id: Long): Flow<Page?>

    @Query("DELETE FROM pages WHERE comicId = :comicId")
    suspend fun deleteByComicId(comicId: Long)
}
