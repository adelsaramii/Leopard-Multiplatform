package com.attendance.leopard.data.model

data class User(
    val id: String,
    val firstName: String,
    val lastName: String,
    val roleName: String,
    val hasSubordinate: Boolean,
    val fullName: String,
    val code: String?
)

fun User.toSubordinate(): Subordinate {
    return Subordinate(
        fullName = "$firstName $lastName",
        personCode = code?:"",
        personId = id,
    )
}
val userMock = User(
    "f868579e-b84d-44e0-81ce-b693def620cf",
    "Zahra",
    "Mahdifard",
    "",
    false,
    "Zahra Mahdifard",
    "14011812"
)