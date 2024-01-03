package com.attendance.leopard.data.model

data class WorkPeriod(
    val startDate: String = "",
    val endDate: String = "",
    val id: String = "",
    val name: String = "",
    val isCurrentWorkPeriod: Boolean = false,
)