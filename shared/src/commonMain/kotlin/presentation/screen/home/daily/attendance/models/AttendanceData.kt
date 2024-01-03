package com.attendace.leopard.presentation.screen.home.daily.attendance.models

import com.attendance.leopard.data.model.Attendance

data class AttendanceData(
    val enter: Attendance,
    val exit: Attendance
)