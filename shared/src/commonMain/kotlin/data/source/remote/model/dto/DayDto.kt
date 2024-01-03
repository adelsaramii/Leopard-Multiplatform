package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.CalendarEventData
import com.attendace.leopard.data.model.Day
import com.attendace.leopard.data.model.DayStatus
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DayDto(
    @SerialName("Date") val date: String,
    @SerialName("IsHoliday") val isHoliday: Boolean,
    @SerialName("IsToday") val isToday: Boolean,
    @SerialName("InWorkperiod") val inWorkperiod: Boolean,
    @SerialName("HaveMissedAttendance") val haveMissedAttendance: Boolean,
    @SerialName("HaveOverTimeRequest") val haveOverTimeRequest: Boolean,
    @SerialName("OverTimeRequestColor") val overTimeRequestColor: String?,
    @SerialName("HaveDeficitRequest") val haveDeficitRequest: Boolean,
    @SerialName("DeficitRequestColor") val deficitRequestColor: String?,
)

fun DayDto.toDay(): Day {
    val events = mutableListOf<CalendarEventData>()
    if (haveOverTimeRequest)
        events.add(CalendarEventData(overTimeRequestColor?:"#fff"))
    if (haveDeficitRequest)
        events.add(CalendarEventData(deficitRequestColor?:"#fff"))
    return Day(
        date,
        isHoliday,
        isToday,
        inWorkperiod,
        haveMissedAttendance,
        haveOverTimeRequest,
        overTimeRequestColor?:"#fff",
        haveDeficitRequest,
        deficitRequestColor?:"#fff",
        dayStatus = when {
            isHoliday -> DayStatus.Holiday
            !inWorkperiod -> DayStatus.Unavailable
            isToday -> DayStatus.Today
            else -> DayStatus.Normal
        },
        events = events
    )
}