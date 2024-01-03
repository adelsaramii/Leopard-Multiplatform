package com.attendace.leopard.presentation.screen.home.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.base.NotLoaded
import com.attendace.leopard.util.theme.daily
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.util.theme.monthly
import com.attendace.leopard.presentation.screen.home.monthly.AppBar
import com.attendance.leopard.data.model.User

@Composable
fun HomeAppBar(
    baseUrl: String,
    accessToken: String,
    onAppBarClick: () -> Unit,
    openMenuDrawer: () -> Unit,
    currentDestination: HomeNavigation,
    modifier: Modifier = Modifier,
    user: LoadableData<User> = NotLoaded,
    onBackPress: () -> Unit = {},
) {
    AppBar(
        title = when (currentDestination) {
            HomeNavigation.Daily -> localization(daily).value
            HomeNavigation.Monthly -> localization(monthly).value
            else -> ""
        },
        user = user,
        onSubordinateClick = onAppBarClick,
        navigationIconId = when (currentDestination) {
            HomeNavigation.Daily -> "arrow_left.xml"
            else -> "ic_bars.xml"
        },
        onNavigationIconClick = {
            when (currentDestination) {
                HomeNavigation.Daily -> {
                    onBackPress()
                }

                else -> {
                    openMenuDrawer()
                }
            }
        },
        modifier = modifier,
        baseUrl = baseUrl,
        accessToken = accessToken,
    )
}

@Parcelize
sealed class HomeNavigation : Parcelable {
    object Daily : HomeNavigation()
    object Monthly : HomeNavigation()
}

@Parcelize
sealed class HomeNavigationHelper : Parcelable {
    object SubordinateSelect : HomeNavigationHelper()
    object MonthSelect : HomeNavigationHelper()
    object Home : HomeNavigationHelper()
}