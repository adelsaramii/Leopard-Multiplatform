package com.attendace.leopard.presentation.screen.bulk

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.localization
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.white
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun BulkAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    onTitleClicked: () -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth(),
    ) {
        IconButton(onClick = { onBackClicked() }) {
            Image(
                painter = painterResource("arrow_left.xml"),
                modifier = Modifier.padding(8.dp).localization(),
                alignment = Alignment.Center,
                contentDescription = "back",
                colorFilter = ColorFilter.tint(white),
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { onTitleClicked() }
                .fillMaxWidth()
                .padding(end = 16.dp),
        ) {

            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                color = neutralLight0Dark10,
            )

            Image(
                painter = painterResource("angle_down.xml"),
                contentDescription = "angle down",
                colorFilter = ColorFilter.tint(white),
            )
        }
    }
}