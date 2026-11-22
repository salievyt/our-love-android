package dev.deocore.outlove.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
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
import dev.deocore.outlove.ui.viewmodel.TasksViewModel
import dev.deocore.outlove.ui.theme.LoveScreen
import dev.deocore.outlove.ui.theme.loveScreenBackground
import dev.deocore.outlove.ui.theme.LoveColors
import dev.deocore.outlove.ui.theme.LoveGradientButton

@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .loveScreenBackground(LoveScreen.Tasks)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp).padding(bottom = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Generate button
        if (uiState.todayTask == null) {
            LoveGradientButton(
                text = "Новое задание для пары",
                onClick = { viewModel.generateTask() },
                icon = Icons.Rounded.AutoAwesome,
                loading = uiState.isGenerating,
            )
        }

        // Today's task
        val task = uiState.todayTask
        if (task != null) {
            Text(
                "Задание на сегодня",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            GlassCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            modifier = Modifier.size(64.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.AutoMirrored.Rounded.Assignment,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(task.title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text(task.description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    if (!task.isCompleted) {
                        LoveGradientButton(
                            text = "Отметить выполненным",
                            onClick = { viewModel.completeTask(task.id) },
                            icon = Icons.Rounded.CheckCircle,
                        )
                    } else {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = LoveColors.Green.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Rounded.Verified, contentDescription = null, tint = LoveColors.Green)
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Задание выполнено!", color = LoveColors.Green, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Task history
        if (uiState.taskHistory.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "История заданий",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            GlassCard {
                Column(modifier = Modifier.fillMaxWidth()) {
                    uiState.taskHistory.forEachIndexed { index, t ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        Icons.Rounded.History,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(t.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                                Text(t.dateAssigned, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            Icon(
                                if (t.isCompleted) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                                contentDescription = null,
                                tint = if (t.isCompleted) LoveColors.Green else MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        if (index < uiState.taskHistory.size - 1) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
