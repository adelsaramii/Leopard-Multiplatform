package com.attendace.leopard.presentation.screen.components.calendar.utils

sealed class ColumnSize {
    object Wrap : ColumnSize()
    class Custom(val size: Int) : ColumnSize()
}
