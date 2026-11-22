package dev.deocore.outlove.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Шкала iOS Dynamic Type, разложенная по слотам Material 3 (см. DESIGN.md).
private fun style(size: Int, weight: FontWeight, lineHeight: Int) = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = weight,
    fontSize = size.sp,
    lineHeight = lineHeight.sp,
    letterSpacing = 0.sp,
)

val Typography = Typography(
    displayLarge = style(57, FontWeight.Bold, 64),
    displayMedium = style(45, FontWeight.Bold, 52),
    displaySmall = style(34, FontWeight.Bold, 41),      // largeTitle
    headlineLarge = style(28, FontWeight.Bold, 34),     // title
    headlineMedium = style(24, FontWeight.Bold, 30),
    headlineSmall = style(22, FontWeight.SemiBold, 28), // title2
    titleLarge = style(20, FontWeight.Normal, 25),      // title3
    titleMedium = style(17, FontWeight.SemiBold, 22),   // headline
    titleSmall = style(15, FontWeight.SemiBold, 20),    // subheadline semibold
    bodyLarge = style(17, FontWeight.Normal, 22),       // body
    bodyMedium = style(15, FontWeight.Normal, 20),      // subheadline
    bodySmall = style(12, FontWeight.Normal, 16),       // caption
    labelLarge = style(15, FontWeight.Medium, 20),
    labelMedium = style(12, FontWeight.Medium, 16),     // caption medium
    labelSmall = style(11, FontWeight.Normal, 13),      // caption2
)
