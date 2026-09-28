package com.ownreader.data.repository

import com.ownreader.data.database.dao.ComicDao
import com.ownreader.data.model.Comic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val comicDao: ComicDao
) {
    suspend fun insertComic(comic: Comic) {
        comicDao.insert(comic)
    }

    suspend fun updateComic(comic: Comic) {
        comicDao.update(comic)
    }

    suspend fun deleteComic(comic: Comic) {
        comicDao.delete(comic)
    }

    fun getComicById(id: Long): Flow<Comic?> {
        return comicDao.getById(id)
    }

    fun getAllComics(): Flow<List<Comic>> {
        return comicDao.getAllComics()
    }

    fun searchComicsByTitle(query: String): Flow<List<Comic>> {
        return comicDao.searchByTitle(query)
    }
}
