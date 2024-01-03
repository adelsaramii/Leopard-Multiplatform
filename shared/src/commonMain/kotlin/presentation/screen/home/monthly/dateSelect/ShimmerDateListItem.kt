package com.attendace.leopard.presentation.screen.home.monthly.dateSelect

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.shimmerColor
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun ShimmerDateListItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Shimmer(
            width = 20.dp,
            height = 20.dp,
            gradientWidth = 20.dp,
            shape = CircleShape,
            shimmerColor = shimmerColor,
            backColor = gray2,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Shimmer(
            width = 300.dp,
            height = 40.dp,
            gradientWidth = 300.dp,
            shape = RectangleShape,
            shimmerColor = shimmerColor,
            backColor = gray2,
            modifier = Modifier.weight(1f)
        )
    }
}