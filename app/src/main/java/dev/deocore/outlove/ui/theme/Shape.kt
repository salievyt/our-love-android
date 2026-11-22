package dev.deocore.outlove.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Радиусы из iOS: 8 / 12 / 14 (кнопка) / 16 (карточка) / 20. */
object LoveRadius {
    val S = 8.dp
    val M = 12.dp
    val Button = 14.dp
    val Card = 16.dp
    val L = 20.dp
}

object LoveSpacing {
    val Screen = 16.dp
    val Section = 20.dp
    val Card = 16.dp
    val CardItem = 12.dp
    val ButtonVertical = 14.dp
}

val LoveShapes = Shapes(
    extraSmall = RoundedCornerShape(LoveRadius.S),
    small = RoundedCornerShape(LoveRadius.M),
    medium = RoundedCornerShape(LoveRadius.Card),
    large = RoundedCornerShape(LoveRadius.L),
    extraLarge = RoundedCornerShape(24.dp),
)
