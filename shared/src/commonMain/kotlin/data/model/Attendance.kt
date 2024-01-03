package com.attendance.leopard.data.model

data class Attendance(
    val time: String,
    val isMissed: Boolean,
    val isEnter: Boolean,
    val cardReaderName: String,
)