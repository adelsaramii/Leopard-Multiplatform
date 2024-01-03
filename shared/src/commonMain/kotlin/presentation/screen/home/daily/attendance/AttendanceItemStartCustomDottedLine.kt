package com.attendace.leopard.presentation.screen.home.daily.attendance

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray

@Composable
fun AttendanceItemStartCustomDottedLine(modifier: Modifier, spacerHeight: Dp = 3.dp) {
    var rotation = 0f
    if (LocalLayoutDirection.current == LayoutDirection.Rtl) {
        rotation = 180f
    }
    Canvas(
        modifier = modifier
            .rotate(rotation)
    ) {
        //vertical
        drawLine(
            color = gray,
            start = Offset(0f, size.height / 4),
            end = Offset(0f, size.height * 3 / 4 + spacerHeight.toPx()),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        //horizontal 1
        drawLine(
            color = gray,
            start = Offset(0f, size.height / 4),
            end = Offset(size.width, size.height / 4),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
        //horizontal 2
        drawLine(
            color = gray,
            start = Offset(0f, size.height * 3 / 4 + spacerHeight.toPx()),
            end = Offset(size.width, size.height * 3 / 4 + spacerHeight.toPx()),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
        )
    }
}