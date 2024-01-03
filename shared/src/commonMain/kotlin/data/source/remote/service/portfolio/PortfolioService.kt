package com.attendace.leopard.data.source.remote.service.portfolio

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.*

interface PortfolioService {

    suspend fun getPortfolio(
        pageNumber: Int?,
        pageSize: Int?,
        requestCode: String?,
        startDate: String?,
        endDate: String?,
        applicantId: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PortfolioDto>>

    suspend fun getPortfolioRequestTypes(): Either<Failure.NetworkFailure, PortfolioRequestTypesModelDto>

    suspend fun modifyPortfolio(
        items: List<ModifyPortfolioInput>
    ): Either<Failure.NetworkFailure, List<ModifyPortfolioResponseDto>>

    suspend fun getPortfolioApplicantApi(): Either<Failure.NetworkFailure, List<PortfolioRequestTypesDto>>

    /*suspend fun getPortfolioApplicant()
    :Either<Failure.NetworkFailure, List<ModifyPortfolioResponseDto>>*/
}
