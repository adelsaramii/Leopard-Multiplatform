package com.attendace.leopard.presentation.screen.request

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.attendace.leopard.presentation.screen.request.filter.RequestFilterScreen
import com.attendace.leopard.presentation.screen.request.list.RequestListScreen
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.RequestViewModel
import com.attendace.leopard.data.model.Request
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import kotlinx.datetime.LocalDate
import org.koin.compose.koinInject

@Composable
fun RequestScreen(
    openMenuDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RequestViewModel = koinInject(),
    requestRouter: Router<RequestNavigation>,
    navigateToRequestDetail: (Request?) -> Unit,
    navigateToRegisterRequest: (String?) -> Unit
) {
    val state by viewModel.state()
    var isFilter = false

    RoutedContent(
        router = requestRouter,
    ) { screen ->
        when (screen) {
            RequestNavigation.Request -> RequestListScreen(
                state = state,
                onFilterClicked = { requestRouter.push(RequestNavigation.RequestFilter) },
                openMenuDrawer = openMenuDrawer,
                navigateToRequestDetail = {
                    navigateToRequestDetail(it)
                },
                navigateToRegisterRequest = {
                    navigateToRegisterRequest(state.userInfo.data?.id)
                },
                isFilter = isFilter,
                onLoadMoreCalled = {
                    viewModel.onLoadMoreCalled()
                },
                refresh = {
                    viewModel.refresh()
                },
                changeRequestCode = {
                    viewModel.changeRequestCode(it)
                },
                searchTextView = {
                    viewModel.searchTextView(it)
                },
            )

            RequestNavigation.RequestFilter -> RequestFilterScreen(
                state = state,
                onFilterApplyClick = { fromDate: LocalDate?, toDate: LocalDate?, statusId: String? ->
                    isFilter = true
                    viewModel.applyFilters(fromDate, toDate, statusId)
                    requestRouter.pop()
                },
                onFilterResetClicked = {
                    isFilter = false
                    viewModel.resetAllFilters()
                    requestRouter.pop()
                },
                onBackClicked = {
                    requestRouter.pop()
                },
                onClickedStatus = {
                    viewModel.setStatusName(it)
                    val statusId = when (it) {
                        "Accepted" -> "0bc9f96a-69b7-611f-891b-82022341d98e"
                        "Deleted" -> "e5834733-d020-4165-4869-a5a10b83beda"
                        "Rejected" -> "ff92b072-37d5-8b32-d65c-9c635c965809"
                        else -> "805af805-eea7-1261-1d9a-8bdc77b139eb"
                    }
                    viewModel.setStatusId(statusId)
                },
                onFromDateChanged = {
                    viewModel.changeFromDate(it)
                },
            ) {
                viewModel.changeToDate(it)
            }

            else -> {}
        }
    }

}
