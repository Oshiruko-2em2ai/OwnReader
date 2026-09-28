package com.ownreader.data.repository

import com.ownreader.data.database.dao.PageDao
import com.ownreader.data.model.Page
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PageRepository @Inject constructor(
    private val pageDao: PageDao
) {
    suspend fun insertPage(page: Page) = pageDao.insert(page)
    suspend fun insertPages(pages: List<Page>) = pages.forEach { pageDao.insert(it) }
    suspend fun updatePage(page: Page) = pageDao.update(page)
    suspend fun deletePage(page: Page) = pageDao.delete(page)
    fun getPagesByComicId(comicId: Long): Flow<List<Page>> = pageDao.getPagesByComicId(comicId)
    fun getPageById(id: Long): Flow<Page?> = pageDao.getById(id)
    suspend fun deleteByComicId(comicId: Long) = pageDao.deleteByComicId(comicId)
}
