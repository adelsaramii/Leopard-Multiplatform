package com.attendace.leopard.presentation.screen.components

import androidx.compose.foundation.lazy.LazyListState


fun LazyListState.getToEndOffset(
    listSize: Int?,
    offsetTriggerRange: Int = 10
): Boolean {
    return listSize?.let {
        this.layoutInfo.visibleItemsInfo.lastOrNull()?.index?.let { it >= listSize - offsetTriggerRange }
    } ?: false
}