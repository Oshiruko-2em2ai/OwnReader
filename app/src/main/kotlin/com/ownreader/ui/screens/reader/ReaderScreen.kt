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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ownreader.data.database.dao.ReadHistoryDao
import com.ownreader.data.model.ReadHistory
import com.ownreader.data.repository.ComicRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipFile
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
    @ApplicationContext private val context: Context,
    private val comicRepository: ComicRepository,
    private val readHistoryDao: ReadHistoryDao
) : ViewModel() {
    private val _pages = MutableStateFlow<List<Uri>>(emptyList())
    val pages: StateFlow<List<Uri>> = _pages.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()
    private var currentComicId: Long? = null

    fun loadComic(folderUriString: String) {
        if (folderUriString.isBlank()) {
            _pages.value = emptyList()
            _currentPage.value = 0
            return
        }

        viewModelScope.launch {
            val comic = comicRepository.getComicByFolderPath(folderUriString).firstOrNull()
            currentComicId = comic?.id
            val savedPage = if (comic != null) {
                readHistoryDao.getByComicId(comic.id).firstOrNull()?.lastReadPageNumber ?: 0
            } else {
                0
            }

            val root = DocumentFile.fromTreeUri(context, Uri.parse(folderUriString)) ?: return@launch
            val imageFiles = mutableListOf<Uri>()
            collectImages(root, imageFiles)
            _pages.value = imageFiles.sortedBy { it.lastPathSegment.orEmpty().lowercase() }
            _currentPage.value = savedPage.coerceIn(0, _pages.value.size.coerceAtLeast(1) - 1)
        }
    }

    fun nextPage() {
        val next = _currentPage.value + 1
        if (next < _pages.value.size) {
            _currentPage.value = next
            persistPageProgress(next)
        }
    }

    fun previousPage() {
        val previous = _currentPage.value - 1
        if (previous >= 0) {
            _currentPage.value = previous
            persistPageProgress(previous)
        }
    }

    private fun persistPageProgress(pageIndex: Int) {
        val comicId = currentComicId ?: return
        viewModelScope.launch {
            val existing = readHistoryDao.getByComicId(comicId).firstOrNull()
            if (existing == null) {
                readHistoryDao.insert(
                    ReadHistory(
                        comicId = comicId,
                        lastReadPageNumber = pageIndex,
                        lastReadAt = System.currentTimeMillis()
                    )
                )
            } else {
                readHistoryDao.update(
                    existing.copy(
                        lastReadPageNumber = pageIndex,
                        lastReadAt = System.currentTimeMillis()
                    )
                )
            }
        }
    }

    private fun collectImages(documentFile: DocumentFile, out: MutableList<Uri>) {
        if (documentFile.isDirectory) {
            documentFile.listFiles().forEach { child ->
                collectImages(child, out)
            }
            return
        }

        val name = documentFile.name.orEmpty().lowercase()
        when {
            isImageFile(name) -> out.add(documentFile.uri)
            name.endsWith(".zip") -> out.addAll(extractZipImages(documentFile.uri))
            name.endsWith(".rar") -> {
                // RAR support is planned for a future phase. Keep as a no-op for now.
            }
        }
    }

    private fun extractZipImages(uri: Uri): List<Uri> {
        val tempDir = File(context.cacheDir, "zip_extract")
        if (!tempDir.exists()) tempDir.mkdirs()

        val tempZip = File.createTempFile("ownreader_zip_", ".zip", tempDir)
        context.contentResolver.openInputStream(uri)?.use { input ->
            FileOutputStream(tempZip).use { output ->
                input.copyTo(output)
            }
        } ?: return emptyList()

        val extractedFiles = mutableListOf<Uri>()
        ZipFile(tempZip).use { zipFile ->
            zipFile.entries().asSequence().forEach { entry ->
                if (!entry.isDirectory && isImageFile(entry.name)) {
                    val extracted = File(tempDir, "${System.nanoTime()}_${entry.name.substringAfterLast('/')}" )
                    zipFile.getInputStream(entry).use { input ->
                        FileOutputStream(extracted).use { output ->
                            input.copyTo(output)
                        }
                    }
                    extractedFiles.add(Uri.fromFile(extracted))
                }
            }
        }
        return extractedFiles
    }

    private fun isImageFile(fileName: String): Boolean {
        val lower = fileName.lowercase()
        return lower.endsWith(".jpg") ||
            lower.endsWith(".jpeg") ||
            lower.endsWith(".png") ||
            lower.endsWith(".webp")
    }
}
