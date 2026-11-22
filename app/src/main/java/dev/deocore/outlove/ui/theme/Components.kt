package dev.deocore.outlove.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/** Фон раздела на весь экран: системный фон + мягкий оттенок раздела. */
@Composable
fun Modifier.loveScreenBackground(screen: LoveScreen): Modifier =
    this.background(MaterialTheme.colorScheme.background).background(screen.brush)

/**
 * Стеклянная карточка как .loveCard() в iOS: радиус 16, полупрозрачная подложка,
 * градиентная обводка pink/purple 30%, мягкая розовая тень.
 */
@Composable
fun LoveCard(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),
    border: Brush? = LoveGradients.BrandStroke,
    contentPadding: Dp = LoveSpacing.Card,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(LoveRadius.Card)
    var base = modifier
        .fillMaxWidth()
        .shadow(
            elevation = 6.dp,
            shape = shape,
            ambientColor = Pink.copy(alpha = 0.08f),
            spotColor = Pink.copy(alpha = 0.12f),
        )
        .clip(shape)
        .background(containerColor)
    if (border != null) base = base.border(1.dp, border, shape)
    Column(modifier = base.padding(contentPadding), content = content)
}

/** Основная кнопка: белый headline на градиенте Brand, радиус 14, на всю ширину. */
@Composable
fun LoveGradientButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    loading: Boolean = false,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LoveRadius.Button))
            .background(LoveGradients.Brand)
            .alpha(if (enabled) 1f else 0.5f)
            .clickable(enabled = enabled && !loading, onClick = onClick)
            .padding(vertical = LoveSpacing.ButtonVertical),
        contentAlignment = Alignment.Center,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
            if (loading) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(8.dp))
            } else if (icon != null) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
            }
            Text(text, style = MaterialTheme.typography.titleMedium, color = Color.White)
        }
    }
}

/** Заголовок-лого с градиентной заливкой (title3, скруглённый, bold). */
@Composable
fun LoveGradientText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = TextStyle(fontFamily = FontFamily.SansSerif, fontWeight = FontWeight.Bold, fontSize = 20.sp),
) {
    Text(text, modifier = modifier, style = style.copy(brush = LoveGradients.Brand))
}

/** Заголовок секции: иконка pink + headline. */
@Composable
fun LoveSectionHeader(title: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Pink, modifier = Modifier.size(22.dp))
        Spacer(Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.titleMedium)
    }
}

/** Подложка-чип: pink 10%, радиус 12 (код приглашения, теги). */
@Composable
fun LoveChip(text: String, modifier: Modifier = Modifier, color: Color = Pink) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(LoveRadius.M))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(text, style = MaterialTheme.typography.labelMedium, color = color)
    }
}
