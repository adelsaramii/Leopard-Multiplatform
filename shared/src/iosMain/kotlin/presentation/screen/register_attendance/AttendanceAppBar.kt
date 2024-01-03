package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.primaryColor
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource


@OptIn(ExperimentalResourceApi::class)
@Composable
fun AttendanceAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onNavigationIconClick: () -> Unit = {},
    onRefresh: () -> Unit = {},
) {

    Row(
        modifier = modifier
            .height(56.dp)
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(4.dp))
            .background(color = white, shape = RoundedCornerShape(4.dp))
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        IconButton(onClick = onNavigationIconClick) {
            Image(
                painter = painterResource("ic_bars.xml"),
                modifier = Modifier
                    .padding(8.dp),
                alignment = Alignment.Center,
                contentDescription = "navigation",
                colorFilter = ColorFilter.tint(primaryColor)
            )
        }

        Text(
            text = title,
            style = MaterialTheme.typography.body1,
            color = primaryColor,
            fontWeight = FontWeight.Medium
        )
        IconButton(onClick = onRefresh) {
            Image(
                painter = painterResource("ic_redo.xml"),
                modifier = Modifier
                    .padding(8.dp),
                alignment = Alignment.Center,
                contentDescription = "refresh",
                colorFilter = ColorFilter.tint(primaryColor)
            )
        }
    }
}