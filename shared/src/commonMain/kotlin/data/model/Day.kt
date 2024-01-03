package com.attendace.leopard.data.model


data class Day(
    val date: String,
    val isHoliday: Boolean,
    val isToday: Boolean,
    val inWorkperiod: Boolean,
    val haveMissedAttendance: Boolean,
    val haveOverTimeRequest: Boolean,
    val overTimeRequestColor: String,
    val haveDeficitRequest: Boolean,
    val deficitRequestColor: String,
    val dayStatus: DayStatus,
    val events: List<CalendarEventData>
)

sealed interface DayStatus {
    object Normal : DayStatus
    object Holiday : DayStatus
    object Unavailable : DayStatus
    object Selected : DayStatus
    object Today : DayStatus
}

data class CalendarEventData(
    val color: String,
)
