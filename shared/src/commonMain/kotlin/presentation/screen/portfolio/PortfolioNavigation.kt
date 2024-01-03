package com.attendace.leopard.presentation.screen.portfolio

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class PortfolioNavigation: Parcelable {
    object Portfolio : PortfolioNavigation()
    object PortfolioFilter : PortfolioNavigation()
}