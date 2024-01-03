package com.attendace.leopard.presentation.screen.home.monthly.summary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.surface
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun ShimmerSummaryListItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Shimmer(
            modifier = Modifier.padding(8.dp),
            width = 60.dp,
            height = 16.dp,
            shape = shapes.small,
        )
        Shimmer(
            modifier = Modifier.padding(8.dp),
            width = 120.dp,
            height = 16.dp,
            shape = shapes.small,
        )
    }
}