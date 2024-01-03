package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Summary
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SummaryDto(
    @SerialName("Id")
    val id: String,
    @SerialName("Value")
    val value: Float,
    @SerialName("BackgroundColor")
    val backgroundColor: String? = "#F4F4F4",
    @SerialName("Color")
    val color: String? = "#F4F4F4",
    @SerialName("IsRequestNeed")
    val isRequestNeed: Boolean,
    @SerialName("Name")
    val name: String,
    @SerialName("RequestNeedCount")
    val requestNeedCount: Int,
    @SerialName("IsSelectable")
    val isSelectable: Boolean,
)


fun SummaryDto.toSummary(): Summary {
    return Summary(
        id,
        value,
        backgroundColor ?: "#F4F4F4",
        color,
        isRequestNeed,
        name,
        requestNeedCount,
        isSelectable
    )
}