package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.AddRequestResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddRequestResponseDto(
    @SerialName("Message")
    val message: String,
    @SerialName("Validate")
    val validate: Boolean,
)

fun AddRequestResponseDto.toDomainModel(): AddRequestResponse {
    return AddRequestResponse(message, validate)
}