package com.attendace.leopard.presentation.screen.components.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.LoadableData
import com.attendace.leopard.util.platform.appVersionCode
import com.attendace.leopard.util.theme.*
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.presentation.screen.components.ErrorPage
import com.attendace.leopard.data.model.RolePermissionModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@Composable
fun DrawerBody(
    rolePermissionModel: LoadableData<RolePermissionModel>,
    currentRoute: MainNavigation?,
    modifier: Modifier = Modifier,
    onHomeNavigationClicked: () -> Unit = {},
    onPortfolioNavigationClicked: () -> Unit = {},
    onRegisterAttendanceNavigationClicked: () -> Unit = {},
    onRequestNavigationClicked: () -> Unit = {},
    onIndexCardNavigationClicked: () -> Unit = {},
    onSalaryNavigationClicked: () -> Unit = {},
    onPersonnelStatusReportClicked: () -> Unit = {},
    onProfileNavigationClicked: () -> Unit = {},
    closeNavDrawer: () -> Unit = {},
    onLogOutClick: () -> Unit = {},
    retry: () -> Unit
) {

    when (rolePermissionModel) {
        is com.attendace.leopard.data.base.Failed -> {
            ErrorPage(
                modifier = Modifier.fillMaxSize(),
                description = localization(error_message).value
            ) {
                retry()
            }
        }

        is com.attendace.leopard.data.base.Loaded -> {
            val drawerItems = arrayListOf<DrawerItemData>()

            if (rolePermissionModel.data.monthly) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "calender.xml",
                        text = localization(monthly).value,
                        route = MainNavigation.Home,
                        onClick = {
                            onHomeNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.portfolio) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "ic_folder.xml",
                        text = localization(portfolio).value,
                        route = MainNavigation.Portfolio,
                        onClick = {
                            onPortfolioNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.registerAttendance) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "ic_place.xml",
                        text = localization(register_attendance).value,
                        route = MainNavigation.RegisterAttendance,
                        onClick = {
                            onRegisterAttendanceNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.requests) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "request.xml",
                        text = localization(my_request).value,
                        route = MainNavigation.Request,
                        onClick = {
                            onRequestNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.indexCard) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "ic_credit_card.xml",
                        text = localization(indexCard).value,
                        route = MainNavigation.IndexCard,
                        onClick = {
                            onIndexCardNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.PaysLip) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "ic_wallet.xml",
                        text = localization(salary).value,
                        route = MainNavigation.Salary,
                        onClick = {
                            onSalaryNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.personnelStatusReport) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "ic_wallet.xml",
                        text = localization(personnel_status_report).value,
                        route = MainNavigation.PersonnelStatusReport,
                        onClick = {
                            onPersonnelStatusReportClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            if (rolePermissionModel.data.myProfile) {
                drawerItems.add(
                    DrawerItemData(
                        icon = "my_profile.xml",
                        text = localization(myProfile).value,
                        route = MainNavigation.Profile,
                        onClick = {
                            onProfileNavigationClicked()
                            closeNavDrawer()
                        },
                    )
                )
            }
            drawerItems.add(
                DrawerItemData(
                    icon = "power.xml",
                    text = localization(logout).value,
                    route = MainNavigation.Logout,
                    onClick = {
                        onLogOutClick()
                        closeNavDrawer()
                    },
                )
            )

            Column(modifier = modifier) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    items(items = drawerItems, key = { it.text }) { item ->
                        DrawerMenuItem(
                            icon = item.icon,
                            text = item.text,
                            onItemClick = item.onClick,
                            isSelected = currentRoute == item.route,
                        )
                    }
                }
                DrawerBottomSection()
            }
        }

        com.attendace.leopard.data.base.Loading -> {
            ShimmerDrawerBody()
        }

        com.attendace.leopard.data.base.NotLoaded -> {
            ShimmerDrawerBody()
        }
    }


}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ColumnScope.DrawerBottomSection() {
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .weight(1f)
            .padding(24.dp),
    ) {
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Image(
                painter = painterResource("logo_leopard_menu.xml"),
                contentDescription = "logo leopard menu",
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "$appVersionCode ${localization(appVersion).value}",
                color = gray,
                style = MaterialTheme.typography.caption,
            )
        }
    }
}


data class DrawerItemData(
    val icon: String,
    val text: String,
    val route: MainNavigation,
    val onClick: () -> Unit,
)
