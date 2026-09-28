package com.ownreader.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ownreader.data.database.dao.ReadHistoryDao
import com.ownreader.data.model.Comic
import com.ownreader.data.repository.ComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val comicRepository: ComicRepository,
    private val readHistoryDao: ReadHistoryDao
) : ViewModel() {
    private val query = MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        comicRepository.getAllComics(),
        readHistoryDao.getAllHistory(),
        query
    ) { comics, history, searchQuery ->
        val recentIds = history.map { it.comicId }.distinct()
        val recent = recentIds.mapNotNull { id -> comics.firstOrNull { it.id == id } }.take(5)
        val filtered = comics.filter { comic ->
            searchQuery.isBlank() || comic.title.contains(searchQuery, ignoreCase = true)
        }
        HomeUiState(comics = filtered, recentComics = recent, query = searchQuery)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState())

    fun setQuery(value: String) {
        query.value = value
    }

    fun addFolder(uri: String, title: String) {
        viewModelScope.launch {
            if (comicRepository.getComicByFolderPath(uri).stateIn(this).value != null) return@launch
            comicRepository.insertComic(
                Comic(
                    title = title.ifBlank { "無題のコミック" },
                    folderPath = uri
                )
            )
        }
    }
}

data class HomeUiState(
    val comics: List<Comic> = emptyList(),
    val recentComics: List<Comic> = emptyList(),
    val query: String = ""
)
