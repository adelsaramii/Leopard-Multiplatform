package com.attendace.leopard.presentation.screen.bulk

sealed class BulkNavigation(val route: String) {

    object Bulk : BulkNavigation("bulk")

    object WorkPeriod : BulkNavigation("workPeriod")
}
