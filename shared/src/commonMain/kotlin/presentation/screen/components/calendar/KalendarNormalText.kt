package com.attendace.leopard.presentation.screen.components.calendar

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CalendarNormalText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    textColor: Color = Color.Magenta,
) {
    Text(
        modifier = modifier.padding(4.dp),
        color = textColor,
        text = text,
        textAlign = TextAlign.Center,
        style = style
    )
}