package com.attendace.leopard.presentation.screen.bulk

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.BulkViewModel
import com.attendace.leopard.presentation.screen.bulk.list.BulkListScreen
import com.attendace.leopard.presentation.screen.home.monthly.dateSelect.DateSelectBottomSheet
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.Summary
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import org.koin.compose.koinInject

@Composable
fun BulkScreen(
    modifier: Modifier = Modifier,
    summary: Summary,
    workperiodId: String,
    selectedSubordinate: Subordinate?,
    selectedDate: String,
    viewModel: BulkViewModel = koinInject(),
    popBackStack: () -> Unit,
    bulkRouter : Router<BulkScreenNavigation>,
    onNextClick: (String, String, List<Bulk>) -> Unit,
) {
    val state by viewModel.state()

    viewModel.setInitialState(
        codeId = summary.id,
        workPeriodId = workperiodId,
        personId = selectedSubordinate?.personId
    )
    viewModel.getWorkPeriods()


    RoutedContent(
        router = bulkRouter,
    ) { screen ->
        when (screen) {
            BulkScreenNavigation.BulkListScreen -> BulkListScreen(
                summary = summary,
                state = state,
                viewModel = viewModel,
                onBackClicked = {
                    popBackStack()
                },
                onWorkPeriodClick = {
                    bulkRouter.push(BulkScreenNavigation.DateSelectBottomSheet)
                },
                onNextClick = { requests ->
                    (selectedSubordinate?.personId
                        ?: state.userInfo.data?.id)?.let { personCode ->
                        onNextClick(
                            personCode, state.codeId, requests
                        )
                    }
                },
                selectedDate = selectedDate,
            )

            BulkScreenNavigation.DateSelectBottomSheet -> DateSelectBottomSheet(
                dates = state.workPeriods,
                onBackPressed = {
                    bulkRouter.pop()
                },
                onItemSelected = {
                    viewModel.selectWorkPeriod(it)
                    bulkRouter.pop()
                },
                onRetry = { viewModel.getWorkPeriods() },
                selectedDate = state.selectedWorkPeriod
            )

        }
    }

}

@Parcelize
sealed class BulkScreenNavigation : Parcelable {
    object BulkListScreen : BulkScreenNavigation()
    object DateSelectBottomSheet : BulkScreenNavigation()
}