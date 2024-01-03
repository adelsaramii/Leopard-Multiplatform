package com.attendance.leopard.data.model

@kotlinx.serialization.Serializable
data class Subordinate(
    val fullName: String = "",
    val personCode: String = "",
    val personId: String = "",
)

fun Subordinate.toUser(): User {
    return User(
        fullName = fullName,
        code = personCode,
        id = personId,
        firstName = "",
        lastName = "",
        roleName = "",
        hasSubordinate = true
    )
}