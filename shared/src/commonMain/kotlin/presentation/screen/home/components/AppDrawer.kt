package com.attendace.leopard.presentation.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.data.model.RolePermissionModel
import com.attendace.leopard.presentation.screen.components.drawer.DrawerBody
import com.attendace.leopard.presentation.screen.components.drawer.DrawerHeader
import com.attendace.leopard.presentation.screen.components.drawer.MainNavigation
import com.attendance.leopard.data.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun AppDrawer(
    rolePermissionModel: LoadableData<RolePermissionModel>,
    currentRoute: MainNavigation?,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
    user: LoadableData<User>,
    baseUrl: String,
    accessToken: String,
    logout: (String?) -> Unit,
    onHomeNavigationClicked: () -> Unit,
    onPortfolioNavigationClicked: () -> Unit,
    onRegisterAttendanceNavigationClicked: () -> Unit,
    onRequestNavigationClicked: () -> Unit,
    onIndexCardNavigationClicked: () -> Unit,
    onSalaryCardNavigationClicked: () -> Unit,
    onPersonnelStatusReportClicked: () -> Unit,
    onProfileNavigationClicked: () -> Unit,
    modifier: Modifier = Modifier,
    bodyRetry: () -> Unit
) {
    Column(modifier = modifier.background(Color.White).width(300.dp)) {
        DrawerHeader(
            user = user,
            baseUrl = baseUrl,
            accessToken = accessToken,
        )
        DrawerBody(
            rolePermissionModel = rolePermissionModel,
            onLogOutClick = {
                logout(null)
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onHomeNavigationClicked = {
                onHomeNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onPortfolioNavigationClicked = {
                onPortfolioNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onRegisterAttendanceNavigationClicked = {
                onRegisterAttendanceNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onRequestNavigationClicked = {
                onRequestNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onIndexCardNavigationClicked = {
                onIndexCardNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onSalaryNavigationClicked = {
                onSalaryCardNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onPersonnelStatusReportClicked = {
                onPersonnelStatusReportClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            onProfileNavigationClicked = {
                onProfileNavigationClicked()
                coroutineScope.launch {
                    drawerState.close()
                }
            },
            currentRoute = currentRoute,
            retry = {
                bodyRetry()
            }
        )
    }
}