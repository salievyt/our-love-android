package dev.deocore.outlove.ui.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

/** Градиенты бренда. Горизонтальные, как leading → trailing в iOS. */
object LoveGradients {
    /** Основной акцент: кнопки, лого, активные элементы */
    val Brand = Brush.horizontalGradient(listOf(Pink, Purple))
    /** Обводка карточки */
    val BrandStroke = Brush.horizontalGradient(listOf(Pink.copy(alpha = 0.3f), Purple.copy(alpha = 0.3f)))
    val Cool = Brush.horizontalGradient(listOf(Blue, Purple))
    val Warm = Brush.horizontalGradient(listOf(Pink, Orange))
}

/** Фон раздела: у каждого экрана свой мягкий оттенок (как LoveScreen в iOS). */
enum class LoveScreen(private val colors: List<Color>, private val diagonal: Boolean) {
    Home(listOf(Pink.copy(alpha = 0.05f), Purple.copy(alpha = 0.05f), Blue.copy(alpha = 0.05f)), true),
    Onboarding(listOf(Pink.copy(alpha = 0.03f), Purple.copy(alpha = 0.03f), Blue.copy(alpha = 0.03f)), true),
    Album(listOf(Blue.copy(alpha = 0.03f), Purple.copy(alpha = 0.03f)), true),
    Map(listOf(Blue.copy(alpha = 0.03f), Purple.copy(alpha = 0.03f)), true),
    Questions(listOf(Blue.copy(alpha = 0.03f), Purple.copy(alpha = 0.03f)), false),
    Diary(listOf(Pink.copy(alpha = 0.03f), Orange.copy(alpha = 0.03f)), false),
    Posters(listOf(Pink.copy(alpha = 0.03f), Orange.copy(alpha = 0.03f)), false),
    Tasks(listOf(Orange.copy(alpha = 0.03f), Yellow.copy(alpha = 0.03f)), false),
    Capsules(listOf(Purple.copy(alpha = 0.03f), Blue.copy(alpha = 0.03f)), false),
    More(listOf(Purple.copy(alpha = 0.03f), Pink.copy(alpha = 0.03f)), false);

    val brush: Brush
        get() = if (diagonal) {
            Brush.linearGradient(colors, start = Offset.Zero, end = Offset.Infinite)
        } else {
            Brush.verticalGradient(colors)
        }
}
