package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.attendace.leopard.presentation.screen.register_attendance.bottomSheet.AttendanceBottomSheet
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel

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

    state.addLogErrorMessage?.let {
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