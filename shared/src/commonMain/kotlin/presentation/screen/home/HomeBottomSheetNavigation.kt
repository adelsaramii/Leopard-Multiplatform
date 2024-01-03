package com.attendace.leopard.presentation.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.attendace.leopard.presentation.screen.home.components.HomeNavigation
import com.attendace.leopard.presentation.screen.home.daily.attendance.tabRow.AttendanceTabRow
import com.attendace.leopard.presentation.viewmodel.HomeViewModel
import com.attendace.leopard.presentation.screen.home.monthly.summary.SummaryBottomSheet
import com.attendance.leopard.data.model.Summary
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter

@Composable
fun HomeBottomSheetNavigation(
    state: HomeViewModel.State,
    retryDailySummary: () -> Unit,
    retryAttendance: () -> Unit,
    onSummaryItemClick: (Summary, String, String) -> Unit,
    router: Router<HomeNavigation>,
    modifier: Modifier = Modifier,
) {

    RoutedContent(
        router = router,
    ) { screen ->
        when (screen) {
            HomeNavigation.Monthly -> SummaryBottomSheet(
                summaryCategory = state.monthlySummary,
                retryDailySummary = retryDailySummary,
                onItemClick = {
                    onSummaryItemClick(
                        it,
                        state.selectedWorkPeriod?.id ?: "",
                        state.selectedWorkPeriod?.name ?: "",
                    )
                }
            )

            HomeNavigation.Daily -> AttendanceTabRow(
                state = state,
                retryDailySummary = retryDailySummary,
                retryAttendance = retryAttendance
            )

            else -> {}
        }
    }

}