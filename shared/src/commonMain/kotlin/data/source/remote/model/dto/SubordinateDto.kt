package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Subordinate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class SubordinateDto(

    @SerialName("PersonId")
    val personId: String,
    @SerialName("PersonCode")
    val personCode: String,
    @SerialName("FullName")
    val fullName: String,
)

fun SubordinateDto.toSubordinate(): Subordinate {
    return Subordinate(fullName, personCode, personId)
}