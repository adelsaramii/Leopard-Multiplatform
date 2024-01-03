package com.attendace.leopard.presentation.screen.profile.list.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun ShimmerProfile(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.size(80.dp))
        Row {
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 142.dp,
                height = 16.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = shimmerBackgroundColor,
            )
        }
        Row {
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 120.dp,
                height = 16.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = shimmerBackgroundColor,
            )
        }
    }
}