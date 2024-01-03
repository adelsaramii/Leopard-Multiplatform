package com.attendance.leopard.data.model

import com.attendace.leopard.data.model.Recorder
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.Type
import com.attendance.leopard.data.source.remote.model.dto.*

data class Log(
    val id: String = "",
    val isMissed: Boolean,
    val latitude: Double,
    val longitude: Double,
    val isOffline: Boolean,
    val recorder: Recorder? = null,
    val date: String,
    val color: String? = null,
    val workflowStatus: Status? = null,
    val workplaceStatus: Status? = null,
    val logType: LogType
)

fun Log.toAddLogInput(isOffline: Boolean, userId: String): AddLogInput {
    return AddLogInput(
        userId = userId,
        date = date,
        latitude = latitude,
        longitude = longitude,
        recorder = recorder?.let { RecorderDto.fromRecorder(recorder) },
        type = when (logType) {
            LogType.Enter -> {
                Type(value = 10001)
            }

            LogType.Exit -> {
                Type(value = 10002)
            }
        },
        isOffline = isOffline
    )
}