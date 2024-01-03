package com.attendance.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.Type
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto
import com.attendace.leopard.data.source.remote.model.dto.toRecorder
import com.attendace.leopard.data.source.remote.model.dto.toWorkplace
import com.attendance.leopard.data.model.DayLog
import com.attendance.leopard.data.model.Log
import com.attendance.leopard.data.model.PairLog
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class DayLogDto(
    @SerialName("LogsCount")
    val logsCount: Int,
    @SerialName("MissedLogsCount")
    val missedLogsCount: Int,
    @SerialName("HasMissedLog")
    val hasMissedLog: Boolean,
    @SerialName("Logs")
    val logs: List<PairLogDto> = emptyList(),
)

@kotlinx.serialization.Serializable
data class PairLogDto(
    @SerialName("Id")
    val id: String,
    @SerialName("Date")
    val date: String,
    @SerialName("HasMissedLog")
    val hasMissedLog: Boolean,
    @SerialName("Enter")
    val enter: LogDto,
    @SerialName("Exit")
    val exit: LogDto,
    @SerialName("Workplace")
    val workplace: WorkplaceDto = WorkplaceDto(
        id = "94f923c9-22f4-4863-8c86-010eb2788a9f",
        name = "Central Office",
        recorders = listOf(),
        latitude = 32.679142,
        longitude = 52.031763,
        radius = 300.0,
        isDeleted = false

    ),
)

@kotlinx.serialization.Serializable
data class LogDto(
    @SerialName("Id")
    val id: String = "",
    @SerialName("Date")
    val date: String = "",
    @SerialName("Latitude")
    val latitude: Double = 0.0,
    @SerialName("Longitude")
    val longitude: Double = 0.0,
    @SerialName("IsMissed")
    val isMissed: Boolean = false,
    @SerialName("IsOffline")
    val isOffline: Boolean = false,
    @SerialName("NeedAction")
    val needAction: Boolean = false,
    @SerialName("Recorder")
    val recorder: RecorderDto? = null,
    @SerialName("Type")
    val type: Type = Type(value = 10001),
    @SerialName("WorkflowStatus")
    val workflowStatus: Type? = null,
    @SerialName("WorkplaceStatus")
    val workplaceStatus: Type? = null,
)

sealed interface Status {
    object Pending : Status
    object Approved : Status
    object Forbidden : Status
    object Unknown : Status
}

sealed interface LogType {
    object Enter : LogType
    object Exit : LogType
}

fun LogDto.toLog(): Log {
    return Log(
        id = id,
        isMissed = isMissed,
        latitude = latitude,
        longitude = longitude,
        isOffline = isOffline,
        recorder = recorder?.toRecorder(),
        color = type.color,
        date = date,
        workflowStatus = when (workflowStatus?.value) {
            11301 -> Status.Pending
            11302 -> Status.Approved
            11303 -> Status.Forbidden
            else -> Status.Unknown
        }, workplaceStatus = when (workplaceStatus?.value) {
            10401 -> Status.Pending
            10402 -> Status.Approved
            10403 -> Status.Forbidden
            else -> Status.Unknown
        },
        logType = when (type.value) {
            10001 -> LogType.Enter
            else -> LogType.Exit
        }
    )
}


fun PairLogDto.toPairLog(): PairLog {
    return PairLog(
        Id = id,
        date = date,
        hasMissedLog = hasMissedLog,
        enter = enter.toLog(),
        exit = exit.toLog(),
        workplace = workplace.toWorkplace()
    )
}


fun DayLogDto.toDayLog(): DayLog {
    return DayLog(
        logsCount = logsCount,
        missedLogsCount = missedLogsCount,
        hasMissedLog = hasMissedLog,
        logs = logs.map { it.toPairLog() }
    )
}

fun DayLogDto.flattenToLog(): List<LogDto> {    //todo check
    return this.logs.flatMap {
        listOf(
            it.enter.copy(recorder = it.workplace.recorders.firstOrNull()),
            it.exit.copy(recorder = it.workplace.recorders.firstOrNull())
        )
    }
}

fun DayLogDto.toDomainModel(): DayLog {
    return DayLog(logsCount, missedLogsCount, hasMissedLog, logs.map { it.toPairLog() })
}





