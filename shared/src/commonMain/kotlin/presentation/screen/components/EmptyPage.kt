package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.no_data

@Composable
fun EmptyPage(
    modifier: Modifier = Modifier,
    message: String = "There is no data!"
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = localization(no_data).value)
    }
}