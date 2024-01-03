package com.attendace.leopard.presentation.screen.components

import androidx.compose.runtime.Composable

@Composable
actual fun NeverOverScroll(content: @Composable () -> Unit) {
    content()
}