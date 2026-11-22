package dev.deocore.outlove.ui.screens

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import dev.deocore.outlove.ui.theme.LoveCard
import dev.deocore.outlove.ui.theme.LoveGradients

/**
 * Карточка экрана. Оставлена для совместимости — рисуется как LoveCard (стиль iOS).
 * Для нового кода используйте LoveCard напрямую.
 */
@Composable
fun BentoCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Unspecified,
    borderColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    val border = when (borderColor) {
        Color.Unspecified -> LoveGradients.BrandStroke
        Color.Transparent -> null
        else -> SolidColor(borderColor)
    }
    if (backgroundColor == Color.Unspecified) {
        LoveCard(modifier = modifier, border = border, content = content)
    } else {
        LoveCard(modifier = modifier, containerColor = backgroundColor, border = border, content = content)
    }
}

/** Стеклянная карточка — то же, что LoveCard (экраны уже ссылались на неё, но её не было). */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    LoveCard(modifier = modifier, content = content)
}
