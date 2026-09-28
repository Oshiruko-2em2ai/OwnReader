package com.ownreader.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ownreader.data.model.Comic
import com.ownreader.data.repository.ComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val comicRepository: ComicRepository
) : ViewModel() {
    private val query = kotlinx.coroutines.flow.MutableStateFlow("")

    val uiState: StateFlow<HomeUiState> = combine(
        comicRepository.getAllComics(),
        query
    ) { comics, searchQuery ->
        val filtered = if (searchQuery.isBlank()) {
            comics
        } else {
            comics.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
        HomeUiState(comics = filtered, query = searchQuery)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState()
    )

    fun setQuery(value: String) {
        query.value = value
    }

    fun addFolder(uri: String, title: String) {
        viewModelScope.launch {
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
    val query: String = ""
)
