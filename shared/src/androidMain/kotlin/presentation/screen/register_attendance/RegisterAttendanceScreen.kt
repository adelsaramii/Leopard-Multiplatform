package com.attendace.leopard.presentation.screen.register_attendance

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.attendace.leopard.data.base.Loading
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.attendace.leopard.util.location.isGpsEnabled
import com.attendace.leopard.util.theme.attendance
import com.attendace.leopard.util.theme.localization
import com.attendance.leopard.android.ui.register_attendance.ButtonState
import com.attendance.leopard.android.ui.register_attendance.RegisterAttendanceButtonRow
import com.attendace.leopard.presentation.screen.register_attendance.near_worlpaces.NearWorkplaceList
import com.attendace.leopard.presentation.viewmodel.util.state
import com.attendace.leopard.util.theme.white
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

const val InitialZoomOnWorkplaceSelect = 16.0
const val AttendanceSheetPeekHeight = 120
const val ButtonMinimumWidth = 160
const val RegisterAttendanceBottomPadding = 156

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalPermissionsApi::class,
    ExperimentalMaterial3Api::class, ExperimentalMaterialNavigationApi::class
)
@SuppressLint("MissingPermission")
@Composable
actual fun RegisterAttendanceScreen(
    viewModel: RegisterAttendanceViewModel,
    openMenuDrawer: () -> Unit,
) {
    val state by viewModel.state()

    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.currentLocation) {
        viewModel.getNearWorkplaces()
    }

    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    val isGpsEnabled by remember {
        mutableStateOf(context.isGpsEnabled())
    }

    val scaffoldState = rememberBottomSheetScaffoldState()

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
        containerColor = Color.White,
        contentColor = Color.White,
        scaffoldState = scaffoldState,
        sheetPeekHeight = AttendanceSheetPeekHeight.dp,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                MapView(state.nearWorkplaces, state.selectedWorkplace , location = state.currentLocation) {
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
                        viewModel.addEnterLog(getCurrentFormattedDate())
                    },
                    onExit = {
                        viewModel.addExitLog(getCurrentFormattedDate())
                    },
                    modifier = Modifier
                        .padding(bottom = RegisterAttendanceBottomPadding.dp)
                        .align(Alignment.BottomCenter)
                )

                var shouldShowPermissionDialog by remember {
                    mutableStateOf(true)
                }
                var shouldShowGpsEnableDialog by remember(shouldShowPermissionDialog) {
                    mutableStateOf(!shouldShowPermissionDialog && !isGpsEnabled)
                }

                LaunchedEffect(locationPermissionState) {
                    if (locationPermissionState.status.isGranted)
                        shouldShowPermissionDialog = false
                }

                LaunchedEffect(locationPermissionState) {
                    if (isGpsEnabled) {
                        shouldShowGpsEnableDialog = false

                        val fusedLocationClient: FusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(activity)

                        fusedLocationClient.lastLocation.addOnSuccessListener {
                            viewModel.setCurrentLocation(it.latitude, it.longitude)
                        }
                    }
                }

                if (shouldShowPermissionDialog) {
                    val textToShow = extractLocationPermissionDialogText(locationPermissionState)
                    LocationPermissionRequestDialog(
                        textToShow = textToShow,
                        onLaunchPermissionClick = {
                            locationPermissionState.launchPermissionRequest()
                            shouldShowPermissionDialog = false
                        },
                        onDismissRequest = {
                            shouldShowPermissionDialog = false
                        },
                    )
                }

            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun extractLocationPermissionDialogText(locationPermissionState: PermissionState) =
    if (locationPermissionState.status.shouldShowRationale) {
        // If the user has denied the permission but the rationale can be shown,
        // then gently explain why the app requires this permission
        "The Location is important for this app. Please grant the permission."
    } else {
        // If it's the first time the user lands on this feature, or the user
        // doesn't want to be asked again for this permission, explain that the
        // permission is required
        "Location permission required for this feature to be available. " +
                "Please grant the permission"
    }

private fun getCurrentFormattedDate(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS ZZZZZ", Locale.ENGLISH)
    return sdf.format(Date())
}
