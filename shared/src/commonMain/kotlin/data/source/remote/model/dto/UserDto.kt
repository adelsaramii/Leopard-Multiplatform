package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.User
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class UserDto(
    @SerialName("FirstName") val firstName: String,
    @SerialName("LastName") val lastName: String,
    @SerialName("RoleName") val roleName: String?,
    @SerialName("HasSubordinate") val hasSubordinate: Boolean,
    @SerialName("Code") val code: String?,
    @SerialName("PersonId") val personId: String,
)

fun UserDto.toUser(): User {
    return User(
        id = personId,
        firstName,
        lastName,
        roleName ?: "",
        hasSubordinate,
        fullName = "$firstName $lastName",
        code = code,
    )
}