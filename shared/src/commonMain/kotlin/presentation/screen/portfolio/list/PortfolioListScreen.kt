package com.attendace.leopard.presentation.screen.portfolio.list

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.presentation.screen.components.LeopardSearch
import com.attendace.leopard.presentation.screen.components.LeopardTabBar
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioConfirmation
import com.attendace.leopard.presentation.screen.portfolio.list.components.PortfolioSelectedCounter
import com.attendace.leopard.presentation.viewmodel.PortfolioViewModel
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.presentation.screen.components.getToEndOffset
import com.attendace.leopard.presentation.screen.components.portfolioGradientBackground
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshState
import com.attendace.leopard.presentation.screen.components.refresh.rememberPullRefreshState
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun PortfolioListScreen(
    viewModel: PortfolioViewModel,
    state: PortfolioViewModel.State,
    isFilter: Boolean,
    onFilterClicked: () -> Unit,
    openMenuDrawer: () -> Unit,
    onItemClick: (Portfolio) -> Unit,
) {
    val pullRefreshState = rememberPullRefreshState(state.isRefreshing, { viewModel.refresh() })

    PortfolioListScreenContent(
        state = state,
        onLoadMoreCalled = { viewModel.onLoadMoreCalled() },
        openMenuDrawer = openMenuDrawer,
        onFilterClicked = {
            onFilterClicked()
        },
        onTabClicked = {
            if (it == "0") state.requestCode = null //Tab -> All
            else state.requestCode = it
            viewModel.changeRequestCode(state.requestCode)
        },
        onConfirmClick = {
            viewModel.confirmPortfolioItem(portfolioItems = it)
        },
        onSearchTextChange = {
            state.searchText = it.text
            viewModel.searchWithDebounce(state.searchText)
        },
        onRejectClick = {
            viewModel.rejectPortfolioItem(portfolioItems = it)
        },
        isFilter = isFilter,
        onItemClick = onItemClick,
        refresh = { viewModel.refresh() },
        pullRefreshState = pullRefreshState,
    )

}

@Composable
fun PortfolioListScreenContent(
    state: PortfolioViewModel.State,
    isFilter: Boolean,
    modifier: Modifier = Modifier,
    openMenuDrawer: () -> Unit,
    onTabClicked: (String) -> Unit = {},
    onLoadMoreCalled: () -> Unit = {},
    onFilterClicked: () -> Unit = {},
    onConfirmClick: (List<Portfolio>) -> Unit,
    onRejectClick: (List<Portfolio>) -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    onItemClick: (Portfolio) -> Unit,
    refresh: () -> Unit,
    pullRefreshState: PullRefreshState,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedCount by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = state.confirmPortfolioResponse) {
        if (state.confirmPortfolioResponse is Loaded) {
            if (state.confirmPortfolioResponse.data.isEmpty()) {
                selectedCount = 0
                snackbarHostState.showSnackbar(
                    localization(done_successfully).value
                )
            } else {
                snackbarHostState.showSnackbar(
                    "${state.confirmPortfolioResponse.data.size} ${localization(item_has_error).value}"
                )
            }

        }
    }

    LaunchedEffect(key1 = state.rejectPortfolioResponse) {
        if (state.rejectPortfolioResponse is Loaded) {
            if (state.rejectPortfolioResponse.data.isEmpty()) {
                selectedCount = 0
                snackbarHostState.showSnackbar(
                    localization(done_successfully).value
                )
            } else {
                snackbarHostState.showSnackbar(
                    "${state.rejectPortfolioResponse.data.size} ${localization(item_has_error).value}"
                )
            }
        }
    }

    Scaffold(snackbarHost = {
        SnackbarHost(
            snackbarHostState
        )
    }) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .portfolioGradientBackground()
        ) {
            AppBar(
                title = localization(portfolio).value,
                onSubordinateClick = {},
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .padding(top = 8.dp),
                onNavigationIconClick = {
                    openMenuDrawer()
                },
                hasRefresh = false,
            )
            val modifierTabBar = if (selectedCount > 0) modifier.clickable(
                enabled = false,
                onClickLabel = null,
                role = null,
                onClick = {})
            else modifier
            LeopardTabBar(list = state.requestTypes,
                modifier = modifierTabBar,
                onTabClicked = { onTabClicked(it) },
                retry = {})
            Box(modifier = Modifier) {
                Column(
                    modifier = Modifier.background(
                        white, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
                ) {
                    val portfoliosListState = rememberLazyListState()
                    LaunchedEffect(portfoliosListState, state.portfolios.data) {
                        snapshotFlow {
                            portfoliosListState.getToEndOffset(listSize = state.portfolios.data?.size)
                        }
                            .filter { shouldLoadMore -> shouldLoadMore }
                            .distinctUntilChanged()
                            .collect { onLoadMoreCalled() }
                    }
                    Box(modifier = Modifier) {
                        if (selectedCount > 0) {
                            PortfolioSelectedCounter(selectedCounter = selectedCount,
                                modifier = Modifier.padding(top = 16.dp),
                                isCheck = selectedCount == (state.portfolios.data?.filter { !it.isHeader }?.size
                                    ?: 0),
                                onCancelClicked = {
                                    state.portfolios.data?.forEach { portfolio ->
                                        portfolio.isSelected = false
                                    }
                                    selectedCount = 0
                                },
                                onSelectAllButton = {
                                    state.portfolios.data?.forEach { portfolio ->
                                        portfolio.isSelected = it
                                    }
                                    selectedCount =
                                        if (it) state.portfolios.data?.filter { it1 -> !it1.isHeader }?.size
                                            ?: 0
                                        else 0
                                })
                        } else {
                            PortfolioSearchFilterRow(
                                modifier = Modifier.padding(16.dp, 16.dp, 16.dp),
                                onFilterClicked = onFilterClicked,
                                onSearchTextChange = { onSearchTextChange(it) },
                                isFilter = isFilter
                            )
                        }
                    }
                    Column(modifier = Modifier) {
                        PortfoliosList(
                            state = state,
                            baseUrl = state.baseUrl,
                            accessToken = state.accessToken,
                            pullRefreshState = pullRefreshState,
                            isRefreshing = state.isRefreshing,
                            onItemClick = onItemClick,
                            modifier = Modifier.weight(1f),
                            retry = {
                                refresh()
                            }
                        ) {
                            selectedCount = it
                        }
                        if (selectedCount > 0)
                            PortfolioConfirmation(confirmLoading = state.confirmPortfolioResponse is Loading,
                                rejectLoading = state.rejectPortfolioResponse is Loading,
                                onConfirmClick = {
                                    onConfirmClick(state.portfolios.data?.filter { it.isSelected }
                                        ?: emptyList())
                                },
                                onRejectClick = {
                                    onRejectClick(state.portfolios.data?.filter { it.isSelected }
                                        ?: emptyList())
                                })
                    }
                }
            }

        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun PortfolioSearchFilterRow(
    isFilter: Boolean,
    onFilterClicked: () -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }
    val isFiltered by remember { mutableStateOf(isFilter) }
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        LeopardSearch(
            modifier = Modifier.weight(1f), onSearchTextChange = {
                searchText = it
                onSearchTextChange(it)
            }, searchText = searchText
        )
        Spacer(modifier = Modifier.width(6.dp))
        val colorIcon: Color
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