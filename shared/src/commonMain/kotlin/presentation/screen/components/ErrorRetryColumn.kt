package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.black
import com.attendace.leopard.util.theme.error_message
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.red
import com.attendace.leopard.util.theme.retry
import com.attendace.leopard.util.theme.white

@Composable
fun ErrorRetryColumn(onRetry: () -> Unit, modifier: Modifier = Modifier, errorTitle: String? = null) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = errorTitle ?: localization(error_message).value, color = black)
        Button(
            onClick = { onRetry() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = red,
                contentColor = white
            ),
        ) {
            Text(text = localization(retry).value)
        }
    }
}