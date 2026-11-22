package dev.deocore.outlove.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.deocore.outlove.ui.viewmodel.AlbumViewModel
import dev.deocore.outlove.ui.theme.LoveScreen
import dev.deocore.outlove.ui.theme.loveScreenBackground
import dev.deocore.outlove.ui.theme.LoveGradientButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumScreen(
    modifier: Modifier = Modifier,
    viewModel: AlbumViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .loveScreenBackground(LoveScreen.Album)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp).padding(bottom = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.collages.isNotEmpty()) {
            Text(
                "Ваши подборки",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                items(uiState.collages.size) { _ ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                        modifier = Modifier.size(width = 160.dp, height = 110.dp).clickable { }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Rounded.CollectionsBookmark,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f),
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        // Year filter
        val years = uiState.photos.map { it.yearTag }.distinct().sortedDescending()
        if (years.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    FilterChip(
                        selected = uiState.selectedYear == null,
                        onClick = { viewModel.selectYear(null) },
                        label = { Text("Все время") },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                years.forEach { year ->
                    item {
                        FilterChip(
                            selected = uiState.selectedYear == year,
                            onClick = { viewModel.selectYear(year) },
                            label = { Text("$year") },
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Photo grid title
        Text(
            "Все фотографии",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Photo grid
        if (uiState.photos.isEmpty()) {
            GlassCard(modifier = Modifier.padding(vertical = 20.dp)) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Rounded.AddPhotoAlternate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Альбом пуст", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Самое время добавить первое фото!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Spacer(modifier = Modifier.height(24.dp))
                    LoveGradientButton(text = "Добавить фото", onClick = { }, icon = Icons.Rounded.Add)
                }
            }
        } else {
            // In a real app we might use a proper grid, but for a scrollable column we use manual rows or fixed height grid
            val rowCount = (uiState.photos.size + 2) / 3
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                for (i in 0 until rowCount) {
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        for (j in 0 until 3) {
                            val index = i * 3 + j
                            if (index < uiState.photos.size) {
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier.weight(1f).aspectRatio(1f).clickable { }
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(Icons.Rounded.Image, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f))
                                    }
                                }
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
