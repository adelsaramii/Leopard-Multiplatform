package com.attendance.leopard.data.source.remote.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class MyRequestTypesModelDto(
    @SerialName("Total") val total: Int,
    @SerialName("RequestCodes") val portfolioRequestTypes: ArrayList<PortfolioRequestTypesDto>
)
