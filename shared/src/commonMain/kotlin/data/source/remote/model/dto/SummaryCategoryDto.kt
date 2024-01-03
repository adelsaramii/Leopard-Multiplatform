package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.SummaryCategory
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SummaryCategoryDto(
    @SerialName("RequestRequired")
    val requestRequired: List<SummaryDto>? = emptyList(),
    @SerialName("TotalInformation")
    val totalInformation: List<SummaryDto>? = emptyList(),
)

fun SummaryCategoryDto.toSummaryCategory(): SummaryCategory {

    return SummaryCategory(
        requestRequired = requestRequired?.map { it.toSummary() } ?: emptyList(),
        totalInformation = totalInformation?.map { it.toSummary() } ?: emptyList()
    )

}