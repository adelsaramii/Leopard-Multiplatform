package com.attendace.leopard.presentation.screen.home.daily.shimmer


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray2
import com.attendace.leopard.util.theme.shapes
import com.attendace.leopard.util.theme.shimmerColor
import com.attendace.leopard.util.theme.surface
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun ShimmerAttendanceListItem(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(surface, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Shimmer(
                height = 64.dp,
                width = 24.dp,
                gradientWidth = 20.dp,
                shape = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp),
                shimmerColor = shimmerColor,
                backColor = gray2,
            )
            Shimmer(
                modifier = Modifier.padding(8.dp),
                width = 32.dp,
                height = 32.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = gray2,
            )
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
            ) {
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 24.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = gray2,
                )
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 40.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = gray2,
                )
            }
        }

        Shimmer(
            modifier = Modifier.padding(8.dp),
            width = 40.dp,
            height = 16.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = gray2,
        )
    }
}