package com.attendace.leopard.presentation.screen.portfolio.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.Shimmer
@Composable
fun ShimmerPortfolioHeader(modifier: Modifier = Modifier) {
    val backColor = shimmerBackgroundColor
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.Transparent, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Shimmer(
                modifier = Modifier
                    .padding(0.dp, 8.dp, 8.dp, 8.dp)
                    .clip(CircleShape),
                width = 40.dp,
                height = 40.dp,
                gradientWidth = 20.dp,
                shape = shapes.small,
                shimmerColor = shimmerColor,
                backColor = backColor,
            )
            Column(
                modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center
            ) {
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 104.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = backColor,
                )
                Shimmer(
                    modifier = Modifier.padding(8.dp),
                    width = 48.dp,
                    height = 12.dp,
                    gradientWidth = 20.dp,
                    shape = shapes.small,
                    shimmerColor = shimmerColor,
                    backColor = backColor,
                )
            }
        }
    }
}