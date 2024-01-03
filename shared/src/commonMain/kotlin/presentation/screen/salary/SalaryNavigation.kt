package com.attendace.leopard.presentation.screen.salary

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class SalaryNavigation : Parcelable {
    object Salary : SalaryNavigation()
    object SubordinateSelect : SalaryNavigation()
}