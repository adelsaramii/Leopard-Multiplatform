package com.attendace.leopard.presentation.screen.components.drawer

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.Summary

@Parcelize
sealed class MainNavigation : Parcelable {
    object Splash : MainNavigation()
    object Auth : MainNavigation()
    object QrScanner : MainNavigation()
    object Home : MainNavigation()
    object Main : MainNavigation()
    object Portfolio : MainNavigation()
    object Request : MainNavigation()
    data class RegisterRequest(
        val request: String? = null,
        val portfolio: String? = null,
        val selectedFormCodeId: String? = null,
        val userId: String? = null,
        val bulkRequests: String? = null,
    ) : MainNavigation()

    object RegisterAttendance : MainNavigation()
    object IndexCard : MainNavigation()
    data class Bulk(
        val summary: String,
        val workperiodId: String,
        val selectedSubordinate: String?,
        val selectedDate: String,
    ) : MainNavigation()

    object Logout : MainNavigation()
    object Salary : MainNavigation()
    object PersonnelStatusReport : MainNavigation()
    object Profile : MainNavigation()
    object AccessDenied : MainNavigation()
}