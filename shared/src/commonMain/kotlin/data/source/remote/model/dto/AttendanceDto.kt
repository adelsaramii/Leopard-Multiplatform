package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Attendance
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AttendanceDto(
    @SerialName("Time") val time: String,
    @SerialName("IsMissed") val isMissed: Boolean,
    @SerialName("IsEnter") val isEnter: Boolean,
    @SerialName("CardReaderName") val cardReaderName: String,
)

fun AttendanceDto.toAttendance(): Attendance {
    return Attendance(
        time = time,
        isMissed = isMissed,
        isEnter = isEnter,
        cardReaderName = cardReaderName
    )
}