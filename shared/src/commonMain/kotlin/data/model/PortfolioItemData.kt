package com.attendance.leopard.data.model

data class PortfolioItemData(
    val isSelected: Boolean,
    val fromDate: String,
    val toDate: String,
    val name: String,
    val registrationDate: String,
    val description: String
)

data class PortfolioSectionData(
    val itemData: List<PortfolioItemData>,
    val user: User
)