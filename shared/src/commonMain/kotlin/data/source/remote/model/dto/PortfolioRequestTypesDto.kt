package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendance.leopard.data.model.Subordinate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PortfolioRequestTypesDto(
    @SerialName("Id") val id: String,
    @SerialName("Name") val name: String,
    @SerialName("Count") val count: Int,
)

fun PortfolioRequestTypesDto.toPortfolioRequestTypes() = LeopardTabBarTypes(
    id, name, count , ""
)

fun PortfolioRequestTypesDto.toSubordinate() = Subordinate(
    name, count.toString(), id
)
