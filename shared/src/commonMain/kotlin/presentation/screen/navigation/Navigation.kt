package com.attendace.leopard.presentation.screen.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.attendace.leopard.data.base.Failed
import com.attendace.leopard.data.base.Loaded
import com.attendace.leopard.presentation.screen.auth.AuthScreen
import com.attendace.leopard.presentation.screen.splash.SplashScreen
import com.attendace.leopard.presentation.viewmodel.SplashViewModel
import com.attendace.leopard.presentation.screen.components.drawer.MainNavigation
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.data.model.Bulk
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.data.model.Request
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.Summary
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.compose.koinInject

@Composable
fun AppNavigation() {

    val router: Router<BaseNavigation> =
        rememberRouter(BaseNavigation::class, stack = listOf(BaseNavigation.Splash))

    val routerChild: Router<MainNavigation> =
        rememberRouter(MainNavigation::class, stack = listOf(MainNavigation.Home))

    val actions = remember(router) { BaseActions(router) }

    RoutedContent(
        router = router,
    ) { screen ->
        when (screen) {
            BaseNavigation.Splash -> {
                val viewModel: SplashViewModel = koinInject()
                val state by viewModel.state()

                state.isLoggedIn?.let { isLoggedIn ->
                    if (!isLoggedIn) actions.navigateToAuth(null)
                    else if (state.workplaceChangesResult is Loaded || state.workplaceChangesResult is Failed) actions.navigateToMain()
                }

                SplashScreen()
            }

            BaseNavigation.Auth -> AuthScreen(
                navigateToHome = actions.navigateToMain
            )

            BaseNavigation.Main -> {
                MainContentNavigation(
                    router = routerChild,
                    navigateToAuth = actions.navigateToAuth
                )
            }

        }
    }

}

class BaseActions(
    router: Router<BaseNavigation>
) {
    val navigateToSplash: () -> Unit = {
        router.replaceCurrent(BaseNavigation.Splash)
    }

    val navigateToAuth: (url: String?) -> Unit = { url ->
        router.replaceCurrent(BaseNavigation.Auth)
    }

    val navigateToMain: () -> Unit = {
        router.replaceCurrent(BaseNavigation.Main)
    }
}

class MainActions(
    router: Router<MainNavigation>,
    currentRoute: State<MainNavigation>? = null,
) {
    val navigateToSplash: () -> Unit = {
        router.replaceCurrent(MainNavigation.Splash)
    }

    val navigateToAuth: (url: String?) -> Unit = { url ->
        router.replaceCurrent(MainNavigation.Auth)
    }

    val navigateToMain: () -> Unit = {
        router.replaceCurrent(MainNavigation.Main)
    }
    val navigateToHome: () -> Unit = {
        var stackAvailable = false
        router.stack.value.backStack.map {
            if (it.configuration == MainNavigation.Home) {
                stackAvailable = true
            }
        }
        if (stackAvailable) {
            router.pop()
        } else {
            router.replaceCurrent(MainNavigation.Home)
        }
    }

    val navigateToPortfolio: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.Portfolio)
    }

    val navigateToProfile: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.Profile)
    }
    val navigateToSalary: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.Salary)
    }

    val navigateToRegisterAttendance: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.RegisterAttendance)
    }
    val navigateToRequest: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.Request)
    }
    val navigateToRequestDetailFormRequest: (request: Request?) -> Unit = { request ->
        router.push(MainNavigation.RegisterRequest(request = request?.let {
            Json.encodeToString(it)
        }))
    }

    val navigateToRegisterRequest: (userId: String?) -> Unit = {
        router.push(MainNavigation.RegisterRequest(userId = it))
    }

    val navigateToRegisterBulkRequest: (userId: String, codeId: String, requests: List<Bulk>) -> Unit =
        { userId, codeId, requests ->
            router.push(
                MainNavigation.RegisterRequest(
                    userId = userId,
                    selectedFormCodeId = codeId,
                    bulkRequests = Json.encodeToString(requests)
                )
            )
        }

    val navigateToRegisterRequestFormPortfolio: (portfolio: Portfolio?) -> Unit = { portfolio ->
        router.push(
            MainNavigation.RegisterRequest(
                portfolio = portfolio?.let {
                    Json.encodeToString(it)
                }
            )
        )
    }
    val navigateToBulkRequest: (summary: Summary, workperiodId: String, selectedDate: String, subordinate: Subordinate?) -> Unit =
        { summary, workperiodId, selectedDate, subordinate ->
            router.push(MainNavigation.Bulk(
                workperiodId = workperiodId,
                summary = summary.let {
                    Json.encodeToString(it)
                },
                selectedDate = selectedDate,
                selectedSubordinate = subordinate?.let {
                    Json.encodeToString(it)
                }
            ))
        }

    val navigateToIndexCard: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.IndexCard)
    }
    val navigateToPersonnelStatusReport: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.PersonnelStatusReport)
    }
    val navigateToAccessDenied: () -> Unit = {
        isFromHome(currentRoute, router, MainNavigation.AccessDenied)
    }

    val navigateToQrCode: () -> Unit = {
        router.replaceCurrent(MainNavigation.QrScanner)
    }

}

fun isFromHome(
    currentRoute: State<MainNavigation>?,
    router: Router<MainNavigation>,
    destination: MainNavigation
) {
    if (currentRoute?.value == MainNavigation.Home) {
        router.push(destination)
    } else {
        router.replaceCurrent(destination)
    }
}

@Parcelize
sealed class BaseNavigation : Parcelable {
    object Splash : BaseNavigation()
    object Auth : BaseNavigation()
    object Main : BaseNavigation()
}