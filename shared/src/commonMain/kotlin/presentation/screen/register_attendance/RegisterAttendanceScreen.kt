package com.attendace.leopard.presentation.screen.register_attendance

import androidx.compose.runtime.Composable
import com.attendace.leopard.presentation.viewmodel.RegisterAttendanceViewModel
import com.attendace.leopard.data.model.Workplace
import com.attendance.leopard.data.source.remote.model.dto.LogType
import org.koin.compose.koinInject

@Composable
expect fun RegisterAttendanceScreen(
    viewModel: RegisterAttendanceViewModel = koinInject(),
    openMenuDrawer: () -> Unit,
)