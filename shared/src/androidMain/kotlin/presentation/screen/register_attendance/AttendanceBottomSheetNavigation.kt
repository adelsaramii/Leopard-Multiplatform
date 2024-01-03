package com.attendace.leopard.presentation.screen.register_attendance

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.attendace.leopard.presentation.screen.register_attendance.bottomSheet.AttendanceBottomSheet
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.attendace.leopard.util.theme.gray
import com.attendace.leopard.util.theme.neutralLight0Dark10
import com.attendace.leopard.util.theme.white
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi

sealed class RegisterAttendanceScreen(val route: String) {
    object TodayLogs : RegisterAttendanceScreen("today-logs")
}


@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun AttendanceBottomSheetNavigation(
    state: RegisterAttendanceViewModel.State,
    modifier: Modifier = Modifier,
    retryAttendance: () -> Unit = {},
    onClearLogErrorMessage: () -> Unit = {},
) {

    val scaffoldState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.addLogErrorMessage) {
        state.addLogErrorMessage?.let {
            scaffoldState.showSnackbar(
                it
            )
        }
    }

    val context = LocalContext.current

    state.addLogErrorMessage?.let {
        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        onClearLogErrorMessage()
    }

    Scaffold(
        modifier,
        snackbarHost = {  androidx.compose.material3.SnackbarHost(scaffoldState)  },
    ) { padding ->
        AttendanceBottomSheet(
            todayLogs = state.todayLogs,
            modifier.padding(padding)
        )
    }

}