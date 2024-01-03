package com.attendace.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Initialize
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class InitializeDto(
    @SerialName("health")
    val health: Boolean,
)

fun InitializeDto.toInitialize(): Initialize {
    return Initialize(
        health
    )
}