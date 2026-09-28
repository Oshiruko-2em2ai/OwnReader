package com.ownreader.data.repository

import com.ownreader.data.database.dao.ComicDao
import com.ownreader.data.model.Comic
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ComicRepository @Inject constructor(
    private val comicDao: ComicDao
) {
    suspend fun insertComic(comic: Comic): Long {
        return comicDao.insert(comic)
    }

    suspend fun updateComic(comic: Comic) {\n        comicDao.update(comic)\n    }\n\n    suspend fun deleteComic(comic: Comic) {\n        comicDao.delete(comic)\n    }\n\n    fun getComicById(id: Long): Flow<Comic?> {\n        return comicDao.getById(id)\n    }\n\n    fun getComicByFolderPath(folderPath: String): Flow<Comic?> {\n        return comicDao.getByFolderPath(folderPath)\n    }\n\n    fun getAllComics(): Flow<List<Comic>> {\n        return comicDao.getAllComics()\n    }\n\n    fun searchComicsByTitle(query: String): Flow<List<Comic>> {\n        return comicDao.searchByTitle(query)\n    }\n}\n