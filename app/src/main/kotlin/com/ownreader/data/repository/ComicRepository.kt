package com.ownreader.data.repository

import com.ownreader.data.database.dao.ComicDao
import com.ownreader.data.model.Comic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val comicDao: ComicDao
) {
    suspend fun insertComic(comic: Comic): Long = comicDao.insert(comic)

    suspend fun updateComic(comic: Comic) = comicDao.update(comic)

    suspend fun deleteComic(comic: Comic) = comicDao.delete(comic)

    fun getComicById(id: Long): Flow<Comic?> = comicDao.getById(id)

    fun getComicByFolderPath(folderPath: String): Flow<Comic?> =
        comicDao.getByFolderPath(folderPath)

    fun getAllComics(): Flow<List<Comic>> = comicDao.getAllComics()

    fun searchComicsByTitle(query: String): Flow<List<Comic>> =
        comicDao.searchByTitle(query)
}
