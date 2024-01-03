package com.attendace.leopard.data.repository.portfolio

import arrow.core.Either
import com.attendace.leopard.data.model.Portfolio
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.*
import com.attendance.leopard.data.source.remote.model.dto.ModifyPortfolioInput
import kotlinx.coroutines.flow.Flow

interface PortfolioRepository {

    suspend fun getPortfolio(
        pageNumber: Int?,
        pageSize: Int?,
        requestCode: String?,
        startDate: String?,
        endDate: String?,
        applicantId: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<Portfolio>>

    suspend fun getPortfolioRequestTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>>

    suspend fun getAccessToken(): Flow<String>
    suspend fun getBaseUrl(): Flow<String>
    suspend fun modifyPortfolio(
        portfolioItems: List<ModifyPortfolioInput>
    ): Either<Failure.NetworkFailure, List<ModifyPortfolioResponse>>

    suspend fun getPortfolioApplicantApi(): Either<Failure.NetworkFailure, List<Subordinate>>
}
