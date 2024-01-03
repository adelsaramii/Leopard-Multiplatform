package com.attendace.leopard.presentation.screen.request

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class RequestNavigation: Parcelable {

    object Request : RequestNavigation()

    object RequestFilter : RequestNavigation()
    object RegisterRequest : RequestNavigation()
    object RegisterFilterStatus : RequestNavigation()
}
