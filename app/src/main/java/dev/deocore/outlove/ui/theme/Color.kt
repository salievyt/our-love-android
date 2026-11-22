package dev.deocore.outlove.ui.theme

import androidx.compose.ui.graphics.Color

// iOS-inspired pink/purple palette
val Pink = Color(0xFFFF2D55)
val PinkLight = Color(0xFFFF6B8A)
val PinkLighter = Color(0xFFFFD6DE)
val PinkGlass = Color(0x33FF2D55)

val Purple = Color(0xFFAF52DE)
val PurpleLight = Color(0xFFC97BEB)
val PurpleLighter = Color(0xFFE8C7F5)
val PurpleGlass = Color(0x33AF52DE)

val Blue = Color(0xFF007AFF)
val BlueLight = Color(0xFF4DA3FF)
val BlueLighter = Color(0xFFB8D8FF)

val Orange = Color(0xFFFF9500)
val OrangeLight = Color(0xFFFFB340)
val OrangeLighter = Color(0xFFFFE0B0)

val Green = Color(0xFF34C759)
val Yellow = Color(0xFFFFCC00)
val Red = Color(0xFFFF3B30)

// Backgrounds
val BackgroundLight = Color(0xFFF5F5F7)
val BackgroundDark = Color(0xFF1C1C1E)
val CardLight = Color(0xFFFFFFFF)
val CardDark = Color(0xFF2C2C2E)

// Glassmorphism
val GlassWhite = Color(0xE8FFFFFF)
val GlassDark = Color(0xE81C1C1E)

// Gradients
val PinkPurpleGradientStart = Pink
val PinkPurpleGradientEnd = Purple
val BluePurpleGradientStart = Blue
val BluePurpleGradientEnd = Purple
val OrangePinkGradientStart = Orange
val OrangePinkGradientEnd = Pink

// Mood colors
val MoodTerrible = Color(0xFFFF3B30)
val MoodBad = Color(0xFFFF9500)
val MoodOkay = Color(0xFFFFCC00)
val MoodGood = Color(0xFF34C759)
val MoodAmazing = Color(0xFFAF52DE)

// ---------------------------------------------------------------------------
// Единый стиль (DESIGN.md в корне проекта). Значения повторяют iOS:
// our-love-ios/Our Love/DesignSystem/LoveTheme.swift — меняйте синхронно.
// ---------------------------------------------------------------------------

/** Именованные цвета бренда — используйте их вместо Color(0xFF...) в экранах. */
object LoveColors {
    val Pink = dev.deocore.outlove.ui.theme.Pink
    val Purple = dev.deocore.outlove.ui.theme.Purple
    val Blue = dev.deocore.outlove.ui.theme.Blue
    val Orange = dev.deocore.outlove.ui.theme.Orange
    val Green = dev.deocore.outlove.ui.theme.Green
    val Yellow = dev.deocore.outlove.ui.theme.Yellow
    val Red = dev.deocore.outlove.ui.theme.Red
}

/** Цвет настроения 1..5: sad, down, neutral, happy, loved (как Mood.color в iOS). */
fun moodColor(mood: Int): Color = when (mood) {
    1 -> MoodTerrible
    2 -> MoodBad
    3 -> MoodOkay
    4 -> MoodGood
    else -> MoodAmazing
}
