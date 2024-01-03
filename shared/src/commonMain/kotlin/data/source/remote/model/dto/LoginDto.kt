package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.Login
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class LoginDto(
    @SerialName("access_token")
    val access_token: String,
    @SerialName("refresh_token")
    val refresh_token: String
)

fun LoginDto.toLogin(): Login {
    return Login(
        access_token, refresh_token
    )
}