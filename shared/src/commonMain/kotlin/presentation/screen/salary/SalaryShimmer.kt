package com.attendace.leopard.presentation.screen.salary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.attendace.leopard.util.theme.white
import com.attendace.leopard.presentation.screen.components.Shimmer

@Composable
fun SalaryShimmer(modifier: Modifier = Modifier) {
    val backColor = shimmerBackgroundColor

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Shimmer(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            width = 160.dp,
            height = 8.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = backColor,
        )
        Shimmer(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            width = 48.dp,
            height = 8.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = backColor,
        )
        Spacer(modifier = Modifier.height(48.dp))
        Column(modifier.weight(1f).background(color = white , RoundedCornerShape(topStart = 16.dp, topEnd = 8.dp) ).padding(top = 16.dp)) {
            repeat(8) {
                SalaryItemShimmer()
            }
        }

    }
}

@Composable
fun SalaryItemShimmer(modifier: Modifier = Modifier) {
    val backColor = shimmerBackgroundColor
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp)
            .background(color = gray2, shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Shimmer(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            width = 48.dp,
            height = 8.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = backColor,
        )
        Shimmer(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            width = 48.dp,
            height = 8.dp,
            gradientWidth = 20.dp,
            shape = shapes.small,
            shimmerColor = shimmerColor,
            backColor = backColor,
        )
    }
}