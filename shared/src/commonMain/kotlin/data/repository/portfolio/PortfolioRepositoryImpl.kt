package com.attendace.leopard.data.repository.portfolio

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendance.leopard.data.model.ModifyPortfolioResponse
import com.attendace.leopard.data.model.Portfolio
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.source.remote.model.dto.ModifyPortfolioInput
import com.attendance.leopard.data.source.remote.model.dto.PortfolioRequestTypesDto
import com.attendance.leopard.data.source.remote.model.dto.toDomainModel
import com.attendance.leopard.data.source.remote.model.dto.toPortfolio
import com.attendance.leopard.data.source.remote.model.dto.toPortfolioRequestTypes
import com.attendance.leopard.data.source.remote.model.dto.toSubordinate
import com.attendace.leopard.data.source.remote.service.portfolio.PortfolioService
import com.attendace.leopard.util.error.Failure
import kotlinx.coroutines.flow.Flow

class PortfolioRepositoryImpl(
    private val portfolioService: PortfolioService,
    private val authSettings: AuthSettings,
) : PortfolioRepository {

    override suspend fun getPortfolio(
        pageNumber: Int?,
        pageSize: Int?,
        requestCode: String?,
        startDate: String?,
        endDate: String?,
        applicantId: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<Portfolio>> {
        return portfolioService.getPortfolio(
            pageNumber = pageNumber,
            pageSize = pageSize,
            requestCode = requestCode,
            startDate = startDate,
            endDate = endDate,
            applicantId = applicantId,
            searchValue = searchValue
        ).map {
            it.map { it.toPortfolio() }
        }
    }

    override suspend fun getPortfolioRequestTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>> {
        return portfolioService.getPortfolioRequestTypes().map { modelDto ->
            val requestType = PortfolioRequestTypesDto("0", "All", modelDto.total)
            modelDto.portfolioRequestTypes.add(0, requestType)
            modelDto.portfolioRequestTypes.map { it.toPortfolioRequestTypes() }
        }
    }

    override suspend fun modifyPortfolio(
        portfolioItems: List<ModifyPortfolioInput>
    ): Either<Failure.NetworkFailure, List<ModifyPortfolioResponse>> {
        return portfolioService.modifyPortfolio(portfolioItems)
            .map { it.map { it.toDomainModel() } }
    }

    override suspend fun getPortfolioApplicantApi(): Either<Failure.NetworkFailure, List<Subordinate>> {
        return portfolioService.getPortfolioApplicantApi().map {
            it.map { it.toSubordinate() }
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
        return authSettings.getAccessToken()
    }

    override suspend fun getBaseUrl(): Flow<String> {
        return authSettings.getBaseUrl()
    }

}
