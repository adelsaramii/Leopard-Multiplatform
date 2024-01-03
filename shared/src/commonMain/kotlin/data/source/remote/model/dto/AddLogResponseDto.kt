package com.attendance.leopard.data.source.remote.model.dto

import kotlinx.serialization.SerialName
@kotlinx.serialization.Serializable
data class AddLogResponseDto(
    @SerialName("Success") val success: Boolean,
    @SerialName("Message") val message: String,
    @SerialName("StatusCode") val statusCode: Int,
//    @SerialName("Data") val logDto: LogDto?,
)