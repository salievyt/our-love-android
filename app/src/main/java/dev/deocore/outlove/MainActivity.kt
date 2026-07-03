package dev.deocore.outlove

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import dev.deocore.outlove.ui.navigation.MainNavigation
import dev.deocore.outlove.ui.theme.OutLoveTheme

/**
 * Главная активность приложения — точка входа.
 *
 * Инициализирует тему OutLove и основную навигацию с нижней панелью вкладок.
 * Edge-to-edge режим обеспечивает отображение контента под системными барами.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Включаем edge-to-edge отображение для Android 15+
        enableEdgeToEdge()
        setContent {
            OutLoveTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Основная навигация с Bottom Navigation Bar
                    MainNavigation()
                }
            }
        }
    }
}
