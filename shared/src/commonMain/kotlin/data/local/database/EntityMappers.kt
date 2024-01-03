package com.attendace.leopard.data.local.database

import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendance.leopard.AddLogInputEntity
import com.attendance.leopard.LogEntity
import com.attendance.leopard.RecorderEntity
import com.attendance.leopard.WorkplaceEntity
import com.attendance.leopard.data.source.remote.model.dto.LogDto
import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.Type
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto

//fun AttendanceEntity.toAttendance(): Attendance =
//    Attendance(
//        time = time,
//        isMissed = isMissed,
//        isEnter = isEnter,
//        cardReaderName = cardReaderName
//    )

//fun LogEntity.toLogDto(recorderDto: RecorderDto): LogDto =
//    LogDto(
//        id = id,
//        isMissed = isMissed,
//        latitude = latitude,
//        longitude = longitude,
//        isOffline = isOffline,
//        recorder = recorderDto,
//        type = type,
//        workflowStatus = workflowStatus,
//        workplaceStatus = workplaceStatus,
//        needAction = needAction,
//    )


fun LogEntity.toLogDto(): LogDto =
    LogDto(
        id = id,
        isMissed = isMissed,
        latitude = latitude,
        longitude = longitude,
        isOffline = isOffline,
        recorder = recorder,
        type = type,
        workflowStatus = workflowStatus,
        workplaceStatus = workplaceStatus,
        needAction = needAction,
        date = date
    )

fun RecorderEntity.toRecorderDto(): RecorderDto =
    RecorderDto(
        id = id,
        name = name,
        radius = radius,
        latitude = latitude,
        longitude = longitude,
        isPrimary = isPrimary,
        isDeleted = isDeleted,
        recorderType = Type(value = 1013) //todo
    )

fun AddLogInputEntity.toAddLogInput() = AddLogInput(
    date = date,
    latitude = latitude,
    longitude = longitude,
    isOffline = isOffline,
    recorder = recorder,
    type = type,
    userId = userId,
)

fun WorkplaceEntity.toWorkplaceDto() = WorkplaceDto(
    id = id,
    name = name,
    radius = radius,
    latitude = latitude,
    longitude = longitude,
    isDeleted = isDeleted,
)
