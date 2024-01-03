package com.attendace.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Log
import com.attendance.leopard.data.source.remote.model.dto.LogType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddLogInput(
    @SerialName("userId")
    val userId: String,
    @SerialName("date")
    val date: String,
    @SerialName("Latitude")
    val latitude: Double,
    @SerialName("Longitude")
    val longitude: Double,
    @SerialName("IsOffline")
    val isOffline: Boolean,
    @SerialName("Recorder")
    val recorder: RecorderDto?,
    @SerialName("Type")
    val type: Type,
)

fun AddLogInput.toLog() =
    Log(
        id = "",
        isMissed = false,
        latitude = latitude,
        longitude = longitude,
        isOffline = isOffline,
        recorder = recorder?.toRecorder(),
        date = date,
        color = null,
        workplaceStatus = null,
        workflowStatus = null,
        logType = when (type.value) {
            10001 -> LogType.Enter
            else -> LogType.Exit
        }
    )