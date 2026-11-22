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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.deocore.outlove.ui.viewmodel.TimeCapsuleViewModel
import dev.deocore.outlove.ui.theme.LoveScreen
import dev.deocore.outlove.ui.theme.loveScreenBackground
import dev.deocore.outlove.ui.theme.LoveColors

@Composable
fun TimeCapsuleScreen(
    modifier: Modifier = Modifier,
    viewModel: TimeCapsuleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .loveScreenBackground(LoveScreen.Capsules)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp).padding(bottom = 30.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Hero
        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(64.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Rounded.HistoryToggleOff,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    "Послание в будущее",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "Напишите письмо себе и своей половинке, которое откроется в особенный день",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = { },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Создать капсулу", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Capsules
        val allCapsules = uiState.activeCapsules + uiState.openedCapsules
        if (allCapsules.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                "Ваши капсулы",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            allCapsules.forEach { capsule ->
                GlassCard(modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = (if (capsule.isReadyToOpen) LoveColors.Green.copy(alpha = 0.1f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                            modifier = Modifier.size(48.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    if (capsule.isOpened) Icons.Rounded.Drafts else Icons.Rounded.Mail,
                                    contentDescription = null,
                                    tint = if (capsule.isReadyToOpen) LoveColors.Green else MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(capsule.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                            Text(
                                if (capsule.isReadyToOpen) "Готова к открытию" else "Откроется ${capsule.openDate}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (capsule.isReadyToOpen) LoveColors.Green else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (capsule.isReadyToOpen) {
                            Icon(Icons.Rounded.LockOpen, contentDescription = null, tint = LoveColors.Orange)
                        } else if (!capsule.isOpened) {
                            Icon(Icons.Rounded.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}
