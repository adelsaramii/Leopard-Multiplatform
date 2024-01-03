package com.attendace.leopard.data.model

import com.attendace.leopard.data.source.remote.model.dto.RecorderDto
import com.attendace.leopard.data.source.remote.model.dto.RecorderType
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto

data class Workplace(
    val id: String = "",
    val isDeleted: Boolean = false,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val radius: Double,
    val recorder: Recorder?,
    var primaryRecorderId: String = ""
)

data class Recorder(
    val id: String,
    val name: String,
    val radius: Double,
    val latitude: Double,
    val longitude: Double,
    val recorderType: RecorderType,
)

fun Recorder.toRecorderDto() = RecorderDto.fromRecorder(this)


fun Workplace.distance(lat2: Double, lon2: Double): Double {
    val theta = longitude - lon2
    var dist: Double = (kotlin.math.sin(deg2rad(latitude))
            * kotlin.math.sin(deg2rad(lat2))
            + (kotlin.math.cos(deg2rad(latitude))
            * kotlin.math.cos(deg2rad(lat2))
            * kotlin.math.cos(deg2rad(theta))))
    dist = kotlin.math.acos(dist)
    dist = rad2deg(dist)
    dist *= 60 * 1.1515
    return dist * 1000
}

private fun deg2rad(deg: Double): Double {
    return deg * kotlin.math.PI / 180.0
}

private fun rad2deg(rad: Double): Double {
    return rad * 180.0 / kotlin.math.PI
}

fun Workplace.toWorkplaceDto() = WorkplaceDto(
    id = id,
    name = name,
    isDeleted = isDeleted,
    latitude = latitude,
    longitude = longitude,
    radius = radius,
    recorders = recorder?.let { listOf(it.toRecorderDto()) } ?: emptyList(),
    primaryRecorderId = primaryRecorderId
)