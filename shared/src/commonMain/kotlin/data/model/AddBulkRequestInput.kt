package com.attendance.leopard.data.model

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddBulkRequestInput(
    @SerialName("FormData") val formData: Map<String, String>,
    @SerialName("Periods") val periods: List<Period>
)

@kotlinx.serialization.Serializable
data class Period(
    @SerialName("StartDate") val startDate: String,
    @SerialName("EndDate") val endDate: String,
)