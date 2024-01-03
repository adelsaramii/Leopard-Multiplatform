package com.attendace.leopard.presentation.screen.profile

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class ProfileNavigation: Parcelable {
    object Profile : ProfileNavigation()
    object Language : ProfileNavigation()
}
