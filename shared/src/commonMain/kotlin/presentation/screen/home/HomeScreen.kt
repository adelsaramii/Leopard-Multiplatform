package com.attendace.leopard.presentation.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.model.Day
import com.attendace.leopard.presentation.screen.components.calendar.CustomCalendar
import com.attendace.leopard.presentation.screen.components.calendar.utils.ColumnSize
import com.attendace.leopard.presentation.screen.home.components.HomeAppBar
import com.attendace.leopard.presentation.screen.home.components.HomeNavigation
import com.attendace.leopard.presentation.screen.home.components.HomeNavigationHelper
import com.attendace.leopard.presentation.screen.home.daily.DailyCalendarHeader
import com.attendace.leopard.presentation.screen.home.monthly.MonthlyCalendarHeader
import com.attendace.leopard.presentation.screen.home.monthly.dateSelect.DateSelectBottomSheet
import com.attendace.leopard.presentation.screen.home.monthly.subordinateSelect.SubordinateSelectBottomSheet
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.presentation.screen.components.leopardGradientBackground
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.HomeViewModel
import com.attendace.leopard.presentation.screen.components.refresh.PullRefreshIndicator
import com.attendace.leopard.presentation.screen.components.refresh.pullRefresh
import com.attendace.leopard.presentation.screen.components.refresh.rememberPullRefreshState
import com.attendace.leopard.util.theme.primaryColor
import com.attendance.leopard.data.model.*
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    openMenuDrawer: () -> Unit,
    navigateToAuthScreen: (String) -> Unit,
    navigateToBulkScreen: (Summary, Subordinate?, String, String) -> Unit,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    homeRouter: Router<HomeNavigation>,
    scaffoldStateHome: BottomSheetScaffoldState,
    viewModel: HomeViewModel = koinInject(),
    homeBaseRouter: Router<HomeNavigationHelper>
) {

    val state by viewModel.state()

    HomeContentNavigation(
        navigateToAuthScreen = navigateToAuthScreen,
        openMenuDrawer = openMenuDrawer,
        modifier = Modifier.fillMaxSize(),
        state = state,
        selectDate = { viewModel.selectDay(it) },
        navigateToBulkScreen = { summary, selectedSubordinate, workperiodId, selectedDate ->
            navigateToBulkScreen(
                summary, selectedSubordinate, workperiodId, selectedDate
            )
        },
        selectWeekPosition = { viewModel.selectWeekPosition(it) },
        selectSubordinate = { viewModel.selectSubordinate(it) },
        selectWorkPeriod = { viewModel.selectWorkPeriod(it) },
        getSubordinates = { viewModel.getSubordinates() },
        getWorkperiodCalendar = { viewModel.getWorkperiodCalendar() },
        getWorkPeriods = { viewModel.getWorkPeriods() },
        refresh = { viewModel.refresh() },
        retryDailySummary = { viewModel.getDailySummary() },
        retryAttendance = { viewModel.getDailyAttendance() },
        retryWorkPeriods = { viewModel.getWorkPeriods() },
        onBackPressed = { onBackPressed() },
        homeBaseRouter = homeBaseRouter,
        scaffoldStateHome = scaffoldStateHome,
        homeRouter = homeRouter
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContentNavigation(
    navigateToAuthScreen: (String) -> Unit,
    navigateToBulkScreen: (Summary, Subordinate?, String, String) -> Unit,
    openMenuDrawer: () -> Unit,
    state: HomeViewModel.State,
    selectDate: (Day) -> Unit,
    refresh: () -> Unit,
    selectWorkPeriod: (WorkPeriod) -> Unit,
    selectWeekPosition: (Int) -> Unit,
    getWorkperiodCalendar: () -> Unit,
    getSubordinates: () -> Unit,
    getWorkPeriods: () -> Unit,
    retryAttendance: () -> Unit,
    retryWorkPeriods: () -> Unit,
    retryDailySummary: () -> Unit,
    scaffoldStateHome: BottomSheetScaffoldState,
    selectSubordinate: (Subordinate) -> Unit,
    modifier: Modifier = Modifier,
    homeRouter: Router<HomeNavigation>,
    onBackPressed: () -> Unit,
    homeBaseRouter: Router<HomeNavigationHelper>,
) {
    RoutedContent(
        router = homeBaseRouter,
    ) { screen ->
        when (screen) {
            HomeNavigationHelper.Home -> HomeDestination(
                state = state,
                navigateToAuthScreen = navigateToAuthScreen,
                refresh = { refresh() },
                selectWorkPeriod = { selectWorkPeriod(it) },
                selectDate = { selectDate(it) },
                onWeekPositionChanged = {
                    selectWeekPosition(it)
                },
                changeWeekPosition = {
                    selectWeekPosition(it)
                },
                retryMonthlyCalendarItems = {
                    getWorkperiodCalendar()
                },
                openMenuDrawer = openMenuDrawer,
                scaffoldStateHome = scaffoldStateHome,
                onSummaryItemClick = { summary, workperiodId, selectedDate ->
                    navigateToBulkScreen(
                        summary, state.selectedSubordinate.data, workperiodId, selectedDate
                    )
                }, retryAttendance = retryAttendance,
                retryDailySummary = retryDailySummary,
                retryWorkPeriods = retryWorkPeriods,
                navigateToMonthSelect = {
                    homeBaseRouter.push(HomeNavigationHelper.MonthSelect)
                },
                homeRouter = homeRouter,
                navigateToSubordinateSelect = {
                    if (homeRouter.stack.value.active.configuration == HomeNavigation.Daily) {
                        homeRouter.pop()
                    }
                    homeBaseRouter.push(HomeNavigationHelper.SubordinateSelect)
                }
            )

            HomeNavigationHelper.MonthSelect -> DateSelectBottomSheet(
                workPeriods = state.workPeriods,
                selectedWorkPeriod = state.selectedWorkPeriod,
                onWorkPeriodSelect = { selectWorkPeriod(it) },
                onRetryWorkPeriods = { getWorkPeriods() },
                onBackPressed = {
                    homeBaseRouter.pop()
                },
            )

            HomeNavigationHelper.SubordinateSelect -> SubordinateSelectBottomSheet(
                state = state,
                onSubordinateSelect = { selectSubordinate(it) },
                onRetrySubordinates = { getSubordinates() },
                onBackPressed = {
                    homeBaseRouter.pop()
                },
            )
        }
    }

}

@Composable
private fun SubordinateSelectBottomSheet(
    state: HomeViewModel.State,
    onSubordinateSelect: (Subordinate) -> Unit,
    onRetrySubordinates: () -> Unit,
    onBackPressed: () -> Unit
) {
    SubordinateSelectBottomSheet(
        subordinates = state.subordinates,
        onBackPressed = onBackPressed,
        onItemSelected = onSubordinateSelect,
        selectedSubordinate = state.selectedSubordinate.data
            ?: state.userInfo.data?.toSubordinate(),
        retry = onRetrySubordinates,
        loadNextItems = { },
    )
}

@Composable
private fun DateSelectBottomSheet(
    workPeriods: LoadableData<List<WorkPeriod>>,
    selectedWorkPeriod: WorkPeriod?,
    onWorkPeriodSelect: (WorkPeriod) -> Unit,
    onRetryWorkPeriods: () -> Unit,
    onBackPressed: () -> Unit,
) {
    DateSelectBottomSheet(
        dates = workPeriods,
        onBackPressed = onBackPressed,
        onItemSelected = onWorkPeriodSelect,
        selectedDate = selectedWorkPeriod,
        onRetry = onRetryWorkPeriods,
        loadNextItems = { },
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun HomeDestination(
    state: HomeViewModel.State,
    navigateToAuthScreen: (String) -> Unit,
    refresh: () -> Unit,
    selectWorkPeriod: (WorkPeriod) -> Unit,
    selectDate: (Day) -> Unit,
    onWeekPositionChanged: (Int) -> Unit,
    changeWeekPosition: (Int) -> Unit,
    onSummaryItemClick: (Summary, String, String) -> Unit,
    retryMonthlyCalendarItems: () -> Unit,
    retryAttendance: () -> Unit,
    retryDailySummary: () -> Unit,
    retryWorkPeriods: () -> Unit,
    openMenuDrawer: () -> Unit,
    modifier: Modifier = Modifier,
    homeRouter: Router<HomeNavigation>,
    scaffoldStateHome: BottomSheetScaffoldState,
    navigateToMonthSelect: () -> Unit,
    navigateToSubordinateSelect: () -> Unit
) {

    val currentDestination: MutableState<HomeNavigation> =
        remember { mutableStateOf(HomeNavigation.Monthly) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isRefreshing, onRefresh = refresh
    )

    LaunchedEffect(key1 = state.shouldNavigateToAuth) {
        if (state.shouldNavigateToAuth) {
            navigateToAuthScreen(state.baseUrl.toString())
        }
    }

    BottomSheetScaffold(
        sheetContent = {
            HomeBottomSheetNavigation(
                state = state,
                modifier = Modifier.fillMaxSize(),
                retryDailySummary = {
                    retryDailySummary()
                },
                retryAttendance = {
                    retryAttendance()
                },
                onSummaryItemClick = onSummaryItemClick,
                router = homeRouter
            )
        },
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        sheetPeekHeight = if (currentDestination.value == HomeNavigation.Daily) {
            500.dp
        } else {
            300.dp
        },
        modifier = modifier,
        scaffoldState = scaffoldStateHome,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
    ) { contentPadding ->
        Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
            Column(
                modifier = Modifier
                    .leopardGradientBackground()
                    .fillMaxSize()
            ) {
                Box(
                    modifier = Modifier
                        .padding(contentPadding)
                        .fillMaxWidth(1f)
                        .wrapContentHeight()
                        .padding(16.dp)
                ) {
                    Column {
                        state.selectedSubordinate.data?.let {
                            HomeAppBar(user = Loaded(it.toUser()),
                                onAppBarClick = {
                                    navigateToSubordinateSelect()
                                },
                                openMenuDrawer = openMenuDrawer,
                                currentDestination = currentDestination.value,
                                baseUrl = state.baseUrl ?: "",
                                accessToken = state.accessToken ?: "",
                                onBackPress = {
                                    homeRouter.pop()
                                    currentDestination.value = HomeNavigation.Monthly
                                })
                        } ?: run {
                            HomeAppBar(user = state.userInfo,
                                onAppBarClick = {
                                    navigateToSubordinateSelect()

                                },
                                openMenuDrawer = openMenuDrawer,
                                currentDestination = currentDestination.value,
                                baseUrl = state.baseUrl ?: "",
                                accessToken = state.accessToken ?: "",
                                onBackPress = {
                                    homeRouter.pop()
                                    currentDestination.value = HomeNavigation.Monthly
                                })
                        }
                        if (state.workPeriods is Failed) {
                            ErrorPage(
                                modifier = Modifier
                                    .fillMaxSize(),
                                description = (state.workPeriods as Failed).failure.getErrorMessage()
                                    ?: ""
                            ) {
                                retryWorkPeriods()
                            }
                        } else {
                            when (currentDestination.value) {
                                HomeNavigation.Daily -> {
                                    DailyCalendarHeader(
                                        workPeriod = state.selectedWorkPeriod,
                                        onPreviousClick = {
                                            if (state.selectWeekPosition > 0) changeWeekPosition(
                                                state.selectWeekPosition - 1
                                            )
                                        },
                                        onNextClick = {
                                            changeWeekPosition(
                                                state.selectWeekPosition + 1
                                            )
                                        },
                                        selectWeekPosition = state.selectWeekPosition,
                                        calendarDayItems = state.monthlyCalendarItems
                                    )
                                }

                                else -> MonthlyCalendarHeader(workperiodItems = state.workPeriods,
                                    workPeriod = state.selectedWorkPeriod,
                                    onPreviousClick = selectWorkPeriod,
                                    onNextClick = selectWorkPeriod,
                                    onMonthChangeClick = {
                                        navigateToMonthSelect()
                                    })
                            }
                            CustomCalendar(modifier = Modifier
                                .padding(8.dp)
                                .wrapContentHeight(),
                                calendarItems = state.monthlyCalendarItems,
                                selectedDay = state.selectedDay,
                                columnSize = when (currentDestination.value) {
                                    HomeNavigation.Daily -> ColumnSize.Custom(1)
                                    else -> ColumnSize.Wrap
                                },
                                onDayClick = {
                                    selectDate(it)
                                    when (currentDestination.value) {
                                        HomeNavigation.Daily -> Unit
                                        else -> {
                                            homeRouter.push(HomeNavigation.Daily)
                                            currentDestination.value = HomeNavigation.Daily
                                        }
                                    }
                                },
                                scrollToForward = state.selectWeekPosition,
                                onWeekPositionChanged = {
                                    onWeekPositionChanged(it)
                                },
                                retry = {
                                    retryMonthlyCalendarItems()
                                })
                        }
                    }
                }
            }

            PullRefreshIndicator(
                state.isRefreshing,
                pullRefreshState,
                Modifier.align(Alignment.TopCenter),
                contentColor = primaryColor
            )
        }
    }

}