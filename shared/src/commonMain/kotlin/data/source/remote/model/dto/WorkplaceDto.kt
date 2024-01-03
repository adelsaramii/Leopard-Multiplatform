package com.attendace.leopard.data.source.remote.model.dto

import com.attendace.leopard.data.model.Recorder
import com.attendace.leopard.data.model.Workplace
import com.attendance.leopard.data.source.remote.model.dto.LogType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class WorkplaceDto(
    @SerialName("Id")
    val id: String = "",
    @SerialName("IsDeleted")
    val isDeleted: Boolean = false,
    @SerialName("Name")
    val name: String = "",
    @SerialName("Latitude")
    val latitude: Double = 0.0,
    @SerialName("Longitude")
    val longitude: Double = 0.0,
    @SerialName("Radius")
    val radius: Double = 0.0,
    @SerialName("Recorders")
    val recorders: List<RecorderDto> = emptyList(),
    var primaryRecorderId : String = ""
)

@kotlinx.serialization.Serializable
data class RecorderDto(
    @SerialName("Id")
    val id: String = "",
    @SerialName("Name")
    val name: String = "",
    @SerialName("Radius")
    val radius: Double = 0.0,
    @SerialName("Latitude")
    val latitude: Double = 0.0,
    @SerialName("Longitude")
    val longitude: Double = 0.0,
    @SerialName("IsPrimary")
    val isPrimary: Boolean = true,
    @SerialName("IsDeleted")
    val isDeleted: Boolean = false,
    @SerialName("Type")
    val recorderType: Type = Type(value = 10101),
) {
    companion object {
        fun fromRecorder(recorder: Recorder) = RecorderDto(
            id = recorder.id,
            name = recorder.name,
            radius = recorder.radius,
            latitude = recorder.latitude,
            longitude = recorder.longitude,
            recorderType = when (recorder.recorderType) {
                RecorderType.GPS -> {
                    Type(value = 10102)
                }
                RecorderType.BLUETOOTH -> {
                    Type(value = 10103)
                }
            },
        )
    }
}

@kotlinx.serialization.Serializable
data class Type(
    @SerialName("Value")
    val value: Int,
    @SerialName("Color")
    val color: String? = null,
    @SerialName("Name")
    val name: String? = null,
) {
    companion object {
        fun fromLogType(logType: LogType) = when (logType) {
            LogType.Enter -> Type(10001)
            LogType.Exit -> Type(10002)
        }
    }
}

fun WorkplaceDto.toWorkplace(): Workplace {
    return Workplace(
        id = id,
        name = name,
        longitude = longitude,
        latitude = latitude,
        radius = radius,
        isDeleted = isDeleted,
        recorder = recorders.firstOrNull { it.isPrimary && !it.isDeleted }?.toRecorder(),
        primaryRecorderId = primaryRecorderId
    )
}

fun RecorderDto.toRecorder(): Recorder {
    return Recorder(
        id = id,
        name = name,
        longitude = longitude,
        latitude = latitude,
        radius = radius,
        recorderType = when (recorderType.value) {
            10103 -> RecorderType.BLUETOOTH
            else -> RecorderType.GPS
        }
    )
}

enum class RecorderType {
    GPS,
    BLUETOOTH
}