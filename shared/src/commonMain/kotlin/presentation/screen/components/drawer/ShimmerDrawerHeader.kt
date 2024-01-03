package com.attendace.leopard.presentation.screen.components.drawer

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.primary
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.shimmerBackgroundColor
import com.attendace.leopard.util.theme.shimmerColor
import com.attendace.leopard.presentation.screen.components.Shimmer
@Composable
fun ShimmerDrawerHeader(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .border(width = 2.dp, color = primary, shape = CircleShape),
        )
        Spacer(modifier = Modifier.size(8.dp))
        Shimmer(
            modifier = Modifier.padding(8.dp),
            width = 142.dp,
            height = 16.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = shimmerBackgroundColor,
        )
        Spacer(modifier = Modifier.size(16.dp))
        Shimmer(
            modifier = Modifier.padding(8.dp),
            width = 64.dp,
            height = 16.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = shimmerBackgroundColor,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}