package dev.deocore.outlove.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Pink,
    onPrimary = Color.White,
    primaryContainer = PinkLighter,
    onPrimaryContainer = Color(0xFF3A001A),
    secondary = Purple,
    onSecondary = Color.White,
    secondaryContainer = PurpleLighter,
    onSecondaryContainer = Color(0xFF2D004F),
    tertiary = Blue,
    onTertiary = Color.White,
    tertiaryContainer = BlueLighter,
    background = BackgroundLight,
    onBackground = Color(0xFF1C1C1E),
    surface = CardLight,
    onSurface = Color(0xFF1C1C1E),
    surfaceVariant = Color(0xFFF2F2F7),
    onSurfaceVariant = Color(0xFF6E6E73),
    outline = Color(0xFFD1D1D6),
    outlineVariant = Color(0xFFE5E5EA),
    error = Red,
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = PinkLight,
    onPrimary = Color(0xFF3A001A),
    primaryContainer = Color(0xFF5C0033),
    onPrimaryContainer = PinkLighter,
    secondary = PurpleLight,
    onSecondary = Color(0xFF2D004F),
    secondaryContainer = Color(0xFF4C007A),
    onSecondaryContainer = PurpleLighter,
    tertiary = BlueLight,
    onTertiary = Color(0xFF001A3D),
    tertiaryContainer = Color(0xFF003366),
    background = BackgroundDark,
    onBackground = Color(0xFFF5F5F7),
    surface = CardDark,
    onSurface = Color(0xFFF5F5F7),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFAEAEB2),
    outline = Color(0xFF48484A),
    outlineVariant = Color(0xFF3A3A3C),
    error = Color(0xFFFF453A),
    onError = Color.White,
)

@Composable
fun OutLoveTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = LoveShapes,
        content = content
    )
}
