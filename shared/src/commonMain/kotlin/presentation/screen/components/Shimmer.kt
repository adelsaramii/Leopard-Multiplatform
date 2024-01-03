package com.attendace.leopard.presentation.screen.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush.Companion.linearGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.shimmerColor

@Composable
fun Shimmer(
    width: Dp,
    height: Dp,
    shape: Shape,
    modifier: Modifier = Modifier,
    gradientWidth: Dp = width,
) {
    Shimmer(
        width = width,
        height = height,
        gradientWidth = gradientWidth,
        shimmerColor = shimmerColor,
        backColor = gray2,
        shape = shape,
        modifier = modifier,
    )
}

@Composable
fun Shimmer(
    width: Dp,
    height: Dp,
    gradientWidth: Dp,
    shape: Shape,
    shimmerColor: Color,
    backColor: Color,
    modifier: Modifier = Modifier
) {
    val cardWidthPx = width.toPx()
    val cardHeightPx = height.toPx()
    val gWidth = gradientWidth.value

    val infiniteTransition = rememberInfiniteTransition()
    val xCardShimmer = infiniteTransition.animateFloat(
        initialValue = (cardWidthPx),
        targetValue = (-gWidth),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
                delayMillis = 200
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    val yCardShimmer = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (cardHeightPx + gWidth),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = LinearEasing,
                delayMillis = 200
            ),
            repeatMode = RepeatMode.Restart
        )
    )

    val colors = listOf(
        backColor,
        shimmerColor,
        backColor,
    )
    val brush = linearGradient(
        colors,
        start = Offset(
            xCardShimmer.value + gradientWidth.value,
            yCardShimmer.value - gradientWidth.value
        ),
        end = Offset(xCardShimmer.value, yCardShimmer.value)
    )
    Spacer(
        modifier = modifier
            .size(width = width, height = height)
            .background(brush = brush, shape = shape)
    )
}