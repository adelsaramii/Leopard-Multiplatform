package com.attendace.leopard.presentation.screen.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.router.stack.pop
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.Request
import com.attendace.leopard.presentation.screen.accessDenied.AccessDeniedScreen
import com.attendace.leopard.presentation.screen.bulk.BulkScreen
import com.attendace.leopard.presentation.screen.bulk.BulkScreenNavigation
import com.attendace.leopard.presentation.screen.components.LogoutDialog
import com.attendace.leopard.presentation.screen.components.drawer.MainNavigation
import com.attendace.leopard.presentation.screen.home.HomeScreen
import com.attendace.leopard.presentation.screen.home.components.AppDrawer
import com.attendace.leopard.presentation.screen.home.components.HomeNavigation
import com.attendace.leopard.presentation.screen.home.components.HomeNavigationHelper
import com.attendace.leopard.presentation.screen.index_card.IndexCardScreen
import com.attendace.leopard.presentation.screen.personnel_status_report.PersonnelStatusReportScreen
import com.attendace.leopard.presentation.screen.portfolio.PortfolioNavigation
import com.attendace.leopard.presentation.screen.portfolio.PortfolioScreen
import com.attendace.leopard.presentation.screen.profile.ProfileScreen
import com.attendace.leopard.presentation.screen.register_attendance.RegisterAttendanceScreen
import com.attendace.leopard.presentation.screen.register_request.RegisterRequestScreen
import com.attendace.leopard.presentation.screen.request.RequestNavigation
import com.attendace.leopard.presentation.screen.request.RequestScreen
import com.attendace.leopard.presentation.screen.salary.SalaryScreen
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.presentation.viewmodel.MainViewModel
import com.attendace.leopard.util.notification.setupNotification
import com.attendance.leopard.data.model.Summary
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContentNavigation(
    navigateToAuth: (String?) -> Unit,
    router: Router<MainNavigation>,
    viewModel: MainViewModel = koinInject()
) {

    val state by viewModel.state()

    val scaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(true) {
        if (!state.addDeviceInfoCalled) {
            coroutineScope.launch {
                setupNotification()
            }
        }
    }

    LaunchedEffect(deviceInput.value) {
        deviceInput.value.let {
            if (it != null) {
                viewModel.addDeviceInfo(it)
            }
        }
    }

    val drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val scaffoldStateHome = rememberBottomSheetScaffoldState()
    val homeBaseRouter: Router<HomeNavigationHelper> = rememberRouter(
        HomeNavigationHelper::class, stack = listOf<HomeNavigationHelper>(HomeNavigationHelper.Home)
    )
    val homeRouter: Router<HomeNavigation> = rememberRouter(
        HomeNavigation::class, stack = listOf(HomeNavigation.Monthly)
    )

    val bulkRouter: Router<BulkScreenNavigation> = rememberRouter(
        BulkScreenNavigation::class, stack = listOf(BulkScreenNavigation.BulkListScreen)
    )

    val portfolioRouter: Router<PortfolioNavigation> = rememberRouter(
        PortfolioNavigation::class, stack = listOf(PortfolioNavigation.Portfolio)
    )

    val requestRouter: Router<RequestNavigation> = rememberRouter(
        RequestNavigation::class, stack = listOf(RequestNavigation.Request)
    )

    val currentRoute: MutableState<MainNavigation> =
        remember { mutableStateOf(MainNavigation.Home) }

    val actions = remember(router) { MainActions(router, currentRoute) }

    LaunchedEffect(state.rolePermissionModel) {
        if (state.rolePermissionModel is Loaded) {
            if (state.rolePermissionModel.data?.monthly == true) return@LaunchedEffect
            else if (state.rolePermissionModel.data?.portfolio == true) actions.navigateToPortfolio()
            else if (state.rolePermissionModel.data?.registerAttendance == true) actions.navigateToRegisterAttendance()
            else if (state.rolePermissionModel.data?.requests == true) actions.navigateToRequest()
            else if (state.rolePermissionModel.data?.indexCard == true) actions.navigateToIndexCard()
            else if (state.rolePermissionModel.data?.PaysLip == true) actions.navigateToSalary()
            else if (state.rolePermissionModel.data?.personnelStatusReport == true) actions.navigateToPersonnelStatusReport()
            else if (state.rolePermissionModel.data?.myProfile == true) actions.navigateToProfile()
            else actions.navigateToAccessDenied()
        }
    }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        AppDrawer(rolePermissionModel = state.rolePermissionModel,
            drawerState = drawerState,
            coroutineScope = coroutineScope,
            user = state.userInfo,
            logout = {
                coroutineScope.launch {
                    scaffoldState.bottomSheetState.expand()
                }
            },
            onHomeNavigationClicked = actions.navigateToHome,
            onPortfolioNavigationClicked = actions.navigateToPortfolio,
            onRegisterAttendanceNavigationClicked = actions.navigateToRegisterAttendance,
            onRequestNavigationClicked = actions.navigateToRequest,
            onIndexCardNavigationClicked = actions.navigateToIndexCard,
            onPersonnelStatusReportClicked = actions.navigateToPersonnelStatusReport,
            onSalaryCardNavigationClicked = actions.navigateToSalary,
            onProfileNavigationClicked = actions.navigateToProfile,
            currentRoute = currentRoute.value,
            baseUrl = state.baseUrl ?: "",
            accessToken = state.accessToken ?: "",
            bodyRetry = {
                viewModel.getRolePermissionModel()
            })
    }) {
        BottomSheetScaffold(
            sheetContent = {
                LogoutDialog(
                    onCloseClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    onCancelClick = {
                        coroutineScope.launch {
                            scaffoldState.bottomSheetState.partialExpand()
                        }
                    },
                    onAcceptClick = {
                        viewModel.logout()
                        navigateToAuth(state.baseUrl)
                        actions.navigateToHome.invoke()
                    },
                )
            },
            sheetContainerColor = Color.White,
            sheetContentColor = Color.White,
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp
        ) {
            RoutedContent(
                router = router,
            ) { screen ->
                when (screen) {

                    MainNavigation.Home -> {
                        currentRoute.value = MainNavigation.Home

                        HomeScreen(
                            openMenuDrawer = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            navigateToAuthScreen = {
                                actions.navigateToAuth(it)
                            },
                            navigateToBulkScreen = { summary, subordinate, workperiodId, selectedDate ->
                                val summaryInput = Summary(
                                    id = summary.id,
                                    name = summary.name,
                                    isRequestNeed = summary.isRequestNeed,
                                    value = summary.value,
                                    isSelectable = summary.isSelectable
                                )
                                actions.navigateToBulkRequest(
                                    summaryInput, workperiodId, selectedDate, subordinate
                                )
                            },
                            onBackPressed = {},
                            homeRouter = homeRouter,
                            scaffoldStateHome = scaffoldStateHome,
                            homeBaseRouter = homeBaseRouter
                        )
                    }

                    MainNavigation.Salary -> {
                        currentRoute.value = MainNavigation.Salary
                        SalaryScreen(
                            openMenuDrawer = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                        )
                    }

                    MainNavigation.Profile -> {
                        currentRoute.value = MainNavigation.Profile
                        ProfileScreen(openMenuDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                    }

                    MainNavigation.Portfolio -> {
                        currentRoute.value = MainNavigation.Portfolio
                        PortfolioScreen(
                            openMenuDrawer = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            portfolioRouter = portfolioRouter,
                            navigateToRequestDetail = { portfolio ->
                                actions.navigateToRegisterRequestFormPortfolio(portfolio)
                            },
                        )
                    }

                    MainNavigation.Request -> {
                        currentRoute.value = MainNavigation.Request
                        RequestScreen(
                            openMenuDrawer = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            },
                            navigateToRequestDetail = {
                                actions.navigateToRequestDetailFormRequest(it)
                            },
                            requestRouter = requestRouter,
                            navigateToRegisterRequest = actions.navigateToRegisterRequest
                        )
                    }

                    is MainNavigation.RegisterRequest -> {
                        currentRoute.value = MainNavigation.RegisterRequest()
                        RegisterRequestScreen(
                            request = screen.request?.let {
                                Json.decodeFromString<Request>(it)
                            },
                            portfolio = screen.portfolio?.let {
                                Json.decodeFromString<Portfolio>(it)
                            },
                            bulkRequests = screen.bulkRequests?.let {
                                Json.decodeFromString<List<Bulk>>(it)
                            },
                            selectedFormCodeId = screen.selectedFormCodeId,
                            userId = screen.userId,
                            popBackStack = {
                                router.pop()
                            },
                        )
                    }

                    MainNavigation.RegisterAttendance -> {
                        currentRoute.value = MainNavigation.RegisterAttendance

                        RegisterAttendanceScreen(openMenuDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                    }

                    is MainNavigation.Bulk -> {
                        currentRoute.value = MainNavigation.Bulk("", "", "", "")
                        BulkScreen(
                            summary = Json.decodeFromString(screen.summary),
                            workperiodId = screen.workperiodId,
                            selectedDate = screen.selectedDate,
                            selectedSubordinate = screen.selectedSubordinate?.let {
                                Json.decodeFromString(
                                    it
                                )
                            },
                            popBackStack = {
                                router.pop()
                            },
                            bulkRouter = bulkRouter,
                            onNextClick = { userId, codeId, requests ->
                                actions.navigateToRegisterBulkRequest(userId, codeId, requests)
                            },
                        )
                    }

                    MainNavigation.IndexCard -> {
                        currentRoute.value = MainNavigation.IndexCard
                        IndexCardScreen(openMenuDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                    }

                    MainNavigation.PersonnelStatusReport -> {
                        currentRoute.value = MainNavigation.PersonnelStatusReport
                        PersonnelStatusReportScreen(openMenuDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                    }

                    MainNavigation.AccessDenied -> {
                        currentRoute.value = MainNavigation.AccessDenied
                        AccessDeniedScreen(openMenuDrawer = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        })
                    }

                    else -> {}
                }
            }

            if (scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = BottomSheetDefaults.ScrimColor)
                        .clickable(
                            interactionSource = MutableInteractionSource(),
                            indication = null
                        ) {
                            coroutineScope.launch {
                                scaffoldState.bottomSheetState.partialExpand()
                            }
                        },
                )
            }
        }
    }

}

val deviceInput = mutableStateOf<AddDeviceInput?>(null)