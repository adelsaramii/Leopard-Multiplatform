package com.attendace.leopard.presentation.screen.register_request

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class RegisterRequestNavigation : Parcelable {
    object RequestForm : RegisterRequestNavigation()
    object RequestTypes : RegisterRequestNavigation()
    object SelectComponent : RegisterRequestNavigation()
}