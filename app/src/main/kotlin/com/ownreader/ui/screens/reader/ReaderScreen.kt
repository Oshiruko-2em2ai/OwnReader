package com.ownreader.ui.screens.reader

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderScreen(
    title: String,
    folderUri: String,
    navController: NavController,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    LaunchedEffect(folderUri) {
        viewModel.loadComic(folderUri)
    }

    val pages by viewModel.pages.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ChevronLeft, contentDescription = "戻る")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.previousPage() },
                    enabled = currentPage > 0
                ) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "前へ")
                }

                Text(
                    text = "${currentPage + 1} / ${pages.size.coerceAtLeast(1)}",
                    style = MaterialTheme.typography.titleMedium
                )

                IconButton(
                    onClick = { viewModel.nextPage() },
                    enabled = currentPage < pages.size - 1
                ) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "次へ")
                }
            }
        }
    ) { paddingValues ->
        if (pages.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("このフォルダに表示できる画像がありません")
            }
            return@Scaffold
        }

        val currentUri = pages[currentPage]
        var scale by remember { mutableFloatStateOf(1f) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) {
                    detectTransformGestures(
                        onGesture = { _, _, zoom, _ ->
                            scale = (scale * zoom).coerceIn(1f, 3f)
                        }
                    )
                }
        ) {
            AsyncImage(
                model = currentUri,
                contentDescription = "ページ ${currentPage + 1}",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { viewModel.previousPage() }) {
                    Text("前")
                }
                Button(onClick = { viewModel.nextPage() }) {
                    Text("次")
                }
            }
        }
    }
}

@HiltViewModel
class ReaderViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : androidx.lifecycle.ViewModel() {
    private val _pages = MutableStateFlow<List<Uri>>(emptyList())
    val pages: StateFlow<List<Uri>> = _pages.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    fun loadComic(folderUriString: String) {
        if (folderUriString.isBlank()) {
            _pages.value = emptyList()
            _currentPage.value = 0
            return
        }

        val root = DocumentFile.fromTreeUri(context, Uri.parse(folderUriString)) ?: return
        val imageFiles = mutableListOf<Uri>()
        collectImages(root, imageFiles)
        _pages.value = imageFiles.sortedBy { it.lastPathSegment.orEmpty().lowercase() }
        _currentPage.value = 0
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next < _pages.value.size) {
            _currentPage.value = next
        }
    }

    fun previousPage() {
        val previous = _currentPage.value - 1
        if (previous >= 0) {
            _currentPage.value = previous
        }
    }

    private fun collectImages(documentFile: DocumentFile, out: MutableList<Uri>) {
        if (documentFile.isDirectory) {
            documentFile.listFiles().forEach { child ->
                collectImages(child, out)
            }
        } else if (isImageFile(documentFile.name.orEmpty())) {
            out.add(documentFile.uri)
        }
    }

    private fun isImageFile(fileName: String): Boolean {
        val lower = fileName.lowercase()
        return lower.endsWith(".jpg") ||
            lower.endsWith(".jpeg") ||
            lower.endsWith(".png") ||
            lower.endsWith(".webp")
    }
}
