package com.attendance.leopard.data.source.remote.model.dto

import com.attendance.leopard.data.model.ModifyPortfolioResponse
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class ModifyPortfolioResponseDto(
    @SerialName("Id") val id: String = "",
)

fun ModifyPortfolioResponseDto.toDomainModel():ModifyPortfolioResponse{
    return ModifyPortfolioResponse(id)
}
