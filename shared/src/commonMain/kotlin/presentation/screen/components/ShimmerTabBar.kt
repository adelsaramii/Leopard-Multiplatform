package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.shimmerBackgroundColor
import com.attendace.leopard.util.theme.shimmerColor

@Composable
fun ShimmerTabBar(modifier: Modifier = Modifier, tabCount: Int) {
    val backColor = shimmerBackgroundColor
    Row(
        modifier = modifier
            .padding(horizontal = 12.dp)
            .wrapContentHeight()
            .background(color = Color.Transparent)
    ) {
        repeat(tabCount) {
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 80.dp,
                height = 28.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = backColor
            )
        }
    }
}