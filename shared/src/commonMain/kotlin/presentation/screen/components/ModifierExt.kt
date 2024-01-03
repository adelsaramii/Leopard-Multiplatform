package com.attendace.leopard.presentation.screen.components

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import com.attendace.leopard.util.localization.LanguageTypeEnum
import com.attendace.leopard.util.theme.language
import com.attendace.leopard.util.theme.splashGradiantEndColor
import com.attendace.leopard.util.theme.splashGradiantStartColor

fun Modifier.leopardGradientBackground(): Modifier =
    background(
        Brush.verticalGradient(
            colors = listOf(
                splashGradiantStartColor, splashGradiantEndColor
            ),
            startY = 0f,
        )
    )

fun Modifier.portfolioGradientBackground() =
    Modifier.background(
        Brush.linearGradient(
            colors = listOf(splashGradiantEndColor, splashGradiantStartColor),
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f),
        )
    )

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFB8B5B5),
                Color(0xFF8F8B8B),
                Color(0xFFB8B5B5),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}

fun Modifier.conditional(
    condition: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: (Modifier.() -> Modifier)? = null
): Modifier {
    return if (condition) {
        then(ifTrue(this))
    } else if (ifFalse != null) {
        then(ifFalse(this))
    } else {
        this
    }
}

fun Modifier.localization() : Modifier {
    return this.rotate(
        if (language.value != LanguageTypeEnum.English) 180F
        else 0F
    )
}