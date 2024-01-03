package com.attendace.leopard.presentation.screen.personnel_status_report

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.attendace.leopard.presentation.screen.components.LeopardSearch
import com.attendace.leopard.presentation.screen.components.LeopardTabBar
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.personnel_status_report.component.PersonnelReportStatusList
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.PersonnelReportStatusViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.getToEndOffset
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.presentation.screen.components.refresh.rememberPullRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PersonnelStatusReportScreen(
    viewModel: PersonnelReportStatusViewModel = koinInject(),
    openMenuDrawer: () -> Unit,
) {
    val state by viewModel.state()
    val snackbarHostState = remember { SnackbarHostState() }
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, { viewModel.refresh() })

    Scaffold(snackbarHost = {
        SnackbarHost(
            snackbarHostState
        )
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .portfolioGradientBackground()
        ) {
            AppBar(
                title = localization(personnel_status_report).value,
                onSubordinateClick = {},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                onNavigationIconClick = {
                    openMenuDrawer()
                },
                hasRefresh = false,
            )

            LeopardTabBar(
                list = state.leopardTabBarTypes,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                onTabClicked = {
                    state.leopardTabBarTypes.data?.map { model ->
                        if (model.id == it) {
                            if (model.status != "All") {
                                viewModel.changeStatus(model.status)
                            } else {
                                viewModel.changeStatus(null)
                            }
                        }
                    }
                }, hasAllInFirst = false,
                retry = {
                    viewModel.retry()
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        white, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            ) {
                LeopardSearch(
                    modifier = Modifier.padding(16.dp, 16.dp, 16.dp),
                    onSearchTextChange = {
                        searchText = it
                        state.searchText = it.text
                        viewModel.searchWithDebounce(state.searchText)
                    }, searchText = searchText
                )

                val personnelStatusReportListState = rememberLazyListState()
                LaunchedEffect(
                    personnelStatusReportListState,
                    state.personnelReportStatus.data
                ) {
                    snapshotFlow {
                        personnelStatusReportListState
                            .getToEndOffset(listSize = state.personnelReportStatus.data?.size)
                    }
                        .filter { shouldLoadMore -> shouldLoadMore }
                        .distinctUntilChanged()
                        .collect {
                            viewModel.onLoadMoreCalled()
                        }
                }
                LaunchedEffect(state.refreshScroll) {
                    personnelStatusReportListState.scrollToItem(0)
                }
                PersonnelReportStatusList(
                    listState = personnelStatusReportListState,
                    paginationData = state.personnelReportStatus,
                    baseUrl = state.baseUrl,
                    accessToken = state.accessToken,
                    pullRefreshState = pullRefreshState,
                    isRefreshing = state.isRefreshing,
                    modifier = Modifier.weight(1f),
                    retry = {
                        viewModel.refresh()
                    }
                )

            }

        }
    }
}