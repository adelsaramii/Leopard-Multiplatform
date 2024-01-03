package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.noDataAvailable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun NoDataPage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            painter = painterResource("ic_opened_box.xml"),
            contentDescription = "opened box",
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = localization(noDataAvailable).value,
            color = gray,
            style = MaterialTheme.typography.subtitle2,
        )
    }
}