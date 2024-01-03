package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.util.theme.attendance
import com.attendace.leopard.util.theme.localization
import com.attendace.leopard.presentation.screen.register_attendance.near_worlpaces.NearWorkplaceList
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.attendace.leopard.util.date.nowLocalDateTimeTimeZone
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.util.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
actual fun RegisterAttendanceScreen(
    viewModel: RegisterAttendanceViewModel,
    openMenuDrawer: () -> Unit,
) {

    val state by viewModel.state()

    val scaffoldState = rememberBottomSheetScaffoldState()

    LaunchedEffect(key1 = state.currentLocation) {
        viewModel.getNearWorkplaces()
    }

    BottomSheetScaffold(
        sheetContent = {
            AttendanceBottomSheetNavigation(
                state = state,
                onClearLogErrorMessage = {
                    viewModel.clearLogErrorMessage()
                },
                modifier = Modifier.fillMaxSize().background(white)
            )
        },
        sheetContainerColor = Color.White,
        sheetContentColor = Color.White,
        scaffoldState = scaffoldState,
        sheetPeekHeight = AttendanceSheetPeekHeight.dp,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                MapView(state.nearWorkplaces, state.selectedWorkplace, { lat, lon ->
                    viewModel.setCurrentLocation(lat, lon)
                }) {
                    viewModel.setSelectedWorkplace(it)
                }
                if (state.nearWorkplaces is Loading || state.todayLogs is Loading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(top = 16.dp),
                ) {

                    AttendanceAppBar(
                        title = localization(attendance).value,
                        onNavigationIconClick = {
                            openMenuDrawer()
                        },
                        onRefresh = {
                            viewModel.observeTodayLogs()
                            viewModel.getNearWorkplaces()
                        },
                        modifier = Modifier.padding(12.dp),
                    )

                    NearWorkplaceList(
                        workplaces = state.nearWorkplaces.data.orEmpty(),
                        selectedWorkplaceId = state.selectedWorkplace?.id,
                        onItemClick = {
                            viewModel.setSelectedWorkplace(it)
                        },
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }

                var enterButtonState by remember { mutableStateOf(ButtonState.Released) }
                var exitButtonState by remember { mutableStateOf(ButtonState.Released) }

                LaunchedEffect(state.enterLoading) {
                    enterButtonState = if (state.enterLoading) {
                        ButtonState.Loading
                    } else {
                        ButtonState.Released
                    }
                }

                LaunchedEffect(state.exitLoading) {
                    exitButtonState = if (state.exitLoading) {
                        ButtonState.Loading
                    } else {
                        ButtonState.Released
                    }
                }

                RegisterAttendanceButtonRow(
                    isRegisterAttendanceEnabled = state.selectedWorkplace != null,
                    enterDrawableId = "drawable_enter.xml",
                    exitDrawableId = "drawable_exit.xml",
                    buttonMinWidth = ButtonMinimumWidth,
                    enterButtonState = enterButtonState,
                    exitButtonState = exitButtonState,
                    onCancel = {},
                    onEnter = {
                        viewModel.addEnterLog(nowLocalDateTimeTimeZone())
                    },
                    onExit = {
                        viewModel.addExitLog(nowLocalDateTimeTimeZone())
                    },
                    modifier = Modifier
                        .padding(bottom = RegisterAttendanceBottomPadding.dp)
                        .align(Alignment.BottomCenter)
                )
            }

        }
    }
}

const val AttendanceSheetPeekHeight = 120
const val ButtonMinimumWidth = 160
const val RegisterAttendanceBottomPadding = 156