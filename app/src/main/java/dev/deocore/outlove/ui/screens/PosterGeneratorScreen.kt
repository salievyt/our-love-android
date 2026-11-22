package dev.deocore.outlove.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.deocore.outlove.ui.viewmodel.PosterGeneratorViewModel
import dev.deocore.outlove.ui.theme.LoveScreen
import dev.deocore.outlove.ui.theme.loveScreenBackground
import dev.deocore.outlove.ui.theme.LoveColors
import dev.deocore.outlove.ui.theme.LoveGradientButton

private enum class PosterType(val title: String, val color: Color, val icon: ImageVector) {
    Days("Дни вместе", LoveColors.Pink, Icons.Rounded.Favorite),
    Achievements("Достижения", LoveColors.Orange, Icons.Rounded.EmojiEvents),
    Map("Карта", LoveColors.Green, Icons.Rounded.Map),
    Stats("Статистика", LoveColors.Blue, Icons.Rounded.BarChart),
}

@Composable
fun PosterGeneratorScreen(
    modifier: Modifier = Modifier,
    viewModel: PosterGeneratorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    var selectedType by remember { mutableStateOf(PosterType.Days) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .loveScreenBackground(LoveScreen.Posters)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp).padding(bottom = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Генератор постеров",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        GlassCard {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text("Выберите стиль", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(12.dp))
                PosterType.entries.forEach { type ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (selectedType == type) type.color.copy(alpha = 0.1f) else Color.Transparent)
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = selectedType == type,
                            onClick = { selectedType = type },
                            colors = RadioButtonDefaults.colors(selectedColor = type.color)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(type.icon, contentDescription = null, tint = if (selectedType == type) type.color else MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(type.title, style = MaterialTheme.typography.bodyMedium, fontWeight = if (selectedType == type) FontWeight.Bold else FontWeight.Normal)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Preview
        Text("Предпросмотр", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth().aspectRatio(0.75f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(selectedType.color.copy(alpha = 0.15f), Color.White)
                        )
                    )
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (selectedType) {
                    PosterType.Days -> {
                        Icon(Icons.Rounded.Favorite, contentDescription = null, tint = selectedType.color, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("${uiState.daysTogether}", fontSize = 80.sp, fontWeight = FontWeight.ExtraBold, color = selectedType.color)
                        Text("дней вместе", style = MaterialTheme.typography.headlineSmall, color = selectedType.color, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Наша история началась ${uiState.startDate}", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                    PosterType.Achievements -> {
                        Icon(Icons.Rounded.EmojiEvents, contentDescription = null, tint = selectedType.color, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Наши успехи", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = selectedType.color)
                        Spacer(modifier = Modifier.height(32.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            AchievementRow(Icons.Rounded.TaskAlt, "Заданий", "${uiState.completedTasks}")
                            AchievementRow(Icons.Rounded.PhotoLibrary, "Фотографий", "${uiState.totalPhotos}")
                            AchievementRow(Icons.Rounded.Place, "Мест", "${uiState.totalPlaces}")
                        }
                    }
                    PosterType.Map -> {
                        Icon(Icons.Rounded.Map, contentDescription = null, tint = selectedType.color, modifier = Modifier.size(80.dp))
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Наш мир", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = selectedType.color)
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("${uiState.totalPlaces}", fontSize = 64.sp, fontWeight = FontWeight.ExtraBold)
                        Text("точек на карте отношений", style = MaterialTheme.typography.bodyLarge)
                    }
                    PosterType.Stats -> {
                        Text("ИТОГИ", style = MaterialTheme.typography.labelLarge, color = selectedType.color, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            PosterStatItem("${uiState.daysTogether}", "Дней", selectedType.color)
                            PosterStatItem(String.format("%.1f", uiState.averageMood), "Улыбок", selectedType.color)
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            PosterStatItem("${uiState.completedTasks}", "Заданий", selectedType.color)
                            PosterStatItem("${uiState.totalPhotos}", "Фото", selectedType.color)
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        LoveGradientButton(text = "Сохранить постер", onClick = { }, icon = Icons.Rounded.Download)
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun AchievementRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PosterStatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, fontSize = 40.sp, fontWeight = FontWeight.ExtraBold, color = color)
        Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Medium)
    }
}
