package com.attendace.leopard.presentation.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.secondaryColor
import com.attendace.leopard.util.theme.white

@Composable
fun LeopardLoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEnabled: Boolean = true,
    colors: ButtonColors = leopardButtonColors(),
    content: @Composable (() -> Unit)
) {
    Button(
        onClick = onClick,
        colors = colors,
        modifier = modifier.sizeIn(minHeight = 48.dp, minWidth = 48.dp),
        enabled = isEnabled && !isLoading
    ) {
        AnimatedVisibility(visible = isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterVertically),
                color = white,
                strokeWidth = 2.dp
            )
        }
        AnimatedVisibility(visible = !isLoading) {
            content()
        }
    }
}

@Composable
fun leopardButtonColors(
    backgroundColor: Color = secondaryColor,
    contentColor: Color = contentColorFor(backgroundColor),
    disabledBackgroundColor: Color = gray,
    disabledContentColor: Color = contentColorFor(backgroundColor) // MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
) =
    ButtonDefaults.buttonColors(
        backgroundColor,
        contentColor,
        disabledBackgroundColor,
        disabledContentColor,
    )

