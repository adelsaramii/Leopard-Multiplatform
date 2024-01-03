package com.attendace.leopard.presentation.screen.request.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.shimmerBackgroundColor
import com.attendace.leopard.util.theme.shimmerColor
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun ShimmerRequestItem(modifier: Modifier = Modifier) {
    val backColor = shimmerBackgroundColor
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(gray2, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
            ) {
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 96.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = backColor,
                )
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 144.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = backColor,
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End
        ) {
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 96.dp,
                height = 12.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = backColor,
            )
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 144.dp,
                height = 12.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = backColor,
            )
        }
    }
}