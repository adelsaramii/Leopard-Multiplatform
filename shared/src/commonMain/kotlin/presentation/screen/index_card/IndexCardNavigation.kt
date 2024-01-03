package com.attendace.leopard.presentation.screen.index_card


sealed class IndexCardNavigation(val route: String) {
    object IndexCard : IndexCardNavigation("indexCard")
    object SubordinateSelect : IndexCardNavigation("subordinate-select")
}