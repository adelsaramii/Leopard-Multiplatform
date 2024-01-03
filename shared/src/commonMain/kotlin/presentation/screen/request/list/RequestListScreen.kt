package com.attendace.leopard.presentation.screen.request.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.PaginationLoadableData
import com.attendace.leopard.presentation.screen.components.LeopardSearch
import com.attendace.leopard.presentation.screen.components.LeopardTabBar
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.viewmodel.RequestViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.getToEndOffset
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.Request
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.rememberPullRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestListScreen(
    isFilter: Boolean,
    state: RequestViewModel.State,
    onFilterClicked: () -> Unit,
    navigateToRequestDetail: (Request) -> Unit,
    navigateToRegisterRequest: () -> Unit,
    onLoadMoreCalled: () -> Unit,
    openMenuDrawer: () -> Unit,
    refresh: () -> Unit,
    changeRequestCode: (String?) -> Unit,
    searchTextView: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, { refresh() })

    Scaffold(
        floatingActionButton = {
            RequestFab {
                navigateToRegisterRequest()
            }
        }
    ) { paddingValues ->
        RequestListScreenContent(
            paginationLoadableDataRequest = state.request,
            requestTypes = state.requestTypes,
            modifier = modifier.padding(paddingValues),
            onLoadMoreCalled = onLoadMoreCalled,
            openMenuDrawer = openMenuDrawer,
            pullRefreshState = pullRefreshState,
            onTabClicked = {
                if (it == "0") state.requestCode = null //Tab -> All
                else state.requestCode = it
                changeRequestCode(state.requestCode)
            },
            state = state,
            onSearchTextChange = {
                state.searchText = it.text
                searchTextView(state.searchText)
            },
            onFilterClicked = onFilterClicked,
            isFilter = isFilter,
            navigateToRequestDetail = navigateToRequestDetail
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun RequestListScreenContent(
    paginationLoadableDataRequest: PaginationLoadableData<List<Request>>,
    requestTypes: LoadableData<List<LeopardTabBarTypes>>,
    pullRefreshState: PullRefreshState,
    isFilter: Boolean,
    modifier: Modifier = Modifier,
    openMenuDrawer: () -> Unit,
    state: RequestViewModel.State,
    onTabClicked: (String) -> Unit = {},
    onLoadMoreCalled: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    refresh: () -> Unit = {},
    onSearchTextChange: (TextFieldValue) -> Unit = {},
    navigateToRequestDetail: (Request) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .portfolioGradientBackground()
    ) {
        AppBar(
            title = localization(my_request).value,
            onSubordinateClick = {},
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .padding(top = 8.dp),
            onNavigationIconClick = {
                openMenuDrawer()
            },
            onTrailingIconClick = {
                refresh()
            },
            hasRefresh = false,
        )

        LeopardTabBar(list = state.requestTypes,
            modifier = Modifier,
            onTabClicked = { onTabClicked(it) },
            retry = {})
        Box(modifier = Modifier) {
            Column(
                modifier = Modifier.background(
                    white, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                )
            ) {
                val requestListState = rememberLazyListState()
                LaunchedEffect(requestListState, paginationLoadableDataRequest.data) {
                    snapshotFlow {
                        requestListState.getToEndOffset(listSize = paginationLoadableDataRequest.data?.size)
                    }
                        .filter { shouldLoadMore -> shouldLoadMore }
                        .distinctUntilChanged()
                        .collect { onLoadMoreCalled() }
                }
                Box(modifier = Modifier) {
                    RequestSearchFilterRow(
                        modifier = Modifier.padding(16.dp, 16.dp, 16.dp),
                        onFilterClicked = onFilterClicked,
                        onSearchTextChange = { onSearchTextChange(it) },
                        isFilter = isFilter,
                    )
                }
                Column(modifier = Modifier) {
                    RequestList(
                        requestListState = requestListState,
                        paginationData = state.request,
                        modifier = Modifier.weight(1f),
                        pullRefreshState = pullRefreshState,
                        isRefreshing = state.isRefreshing
                    ) {
                        navigateToRequestDetail(it)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun RequestSearchFilterRow(
    isFilter: Boolean,
    onFilterClicked: () -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val isFiltered by remember { mutableStateOf(isFilter) }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        LeopardSearch(
            modifier = Modifier.weight(1f),
            onSearchTextChange = {
                //TODO debounce not worked
                searchText = it
                onSearchTextChange(it)
            },
            searchText = searchText,
        )
        Spacer(modifier = Modifier.width(6.dp))
        val colorIcon: Color?
        val backgroundIcon: Color?
        if (isFiltered) {
            colorIcon = white
            backgroundIcon = primary
        } else {
            colorIcon = primary
            backgroundIcon = white
        }
        IconButton(
            onClick = onFilterClicked,
            modifier = Modifier
                .wrapContentHeight()
                .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                .background(color = backgroundIcon, shape = RoundedCornerShape(8.dp))
        ) {
            Image(
                painter = painterResource("ic_filter.xml"),
                contentDescription = "filter",
                colorFilter = ColorFilter.tint(colorIcon)
            )
        }
    }
}


@Composable
private fun RequestFab(
    onClick: () -> Unit
) {
    FloatingActionButton(onClick = {
        onClick()
    }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "add", tint = white)
    }
}