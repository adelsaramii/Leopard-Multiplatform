package com.attendance.leopard.data.source.remote.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class PortfolioRequestTypesModelDto(
    @SerialName("Total") val total: Int,
    @SerialName("PortfolioRequestTypes") val portfolioRequestTypes: ArrayList<PortfolioRequestTypesDto>
)
