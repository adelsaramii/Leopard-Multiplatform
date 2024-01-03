package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.WorkPeriod
import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
data class WorkPeriodDto(
    @SerialName("Id")
    val Id: String,
    @SerialName("StartDate")
    val startDate: String,
    @SerialName("EndDate")
    val endDate: String,
    @SerialName("Name")
    val name: String,
    @SerialName("IsCurrent")
    val isCurrentWorkPeriod: Boolean
)

fun WorkPeriodDto.toWorkPeriod(): WorkPeriod {
    return WorkPeriod(
        startDate = startDate,
        endDate = endDate,
        name = name,
        id = Id,
        isCurrentWorkPeriod = isCurrentWorkPeriod
    )
}