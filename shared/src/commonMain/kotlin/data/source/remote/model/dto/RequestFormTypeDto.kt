package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.RequestFormType
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RequestFormTypeDto(
    @SerialName("Id") val id: String,
    @SerialName("FormName") val name: String,
)

fun RequestFormTypeDto.toDomainModel(): RequestFormType {
    return RequestFormType(
        id, name
    )
}