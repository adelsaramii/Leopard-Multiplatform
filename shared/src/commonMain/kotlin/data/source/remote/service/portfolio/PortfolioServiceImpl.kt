package com.attendace.leopard.data.source.remote.service.portfolio

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.ModifyPortfolioInput
import com.attendance.leopard.data.source.remote.model.dto.ModifyPortfolioResponseDto
import com.attendance.leopard.data.source.remote.model.dto.PortfolioDto
import com.attendance.leopard.data.source.remote.model.dto.PortfolioRequestTypesDto
import com.attendance.leopard.data.source.remote.model.dto.PortfolioRequestTypesModelDto
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class PortfolioServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : PortfolioService {

    override suspend fun getPortfolio(
        pageNumber: Int?,
        pageSize: Int?,
        requestCode: String?,
        startDate: String?,
        endDate: String?,
        applicantId: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PortfolioDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PortfolioApi/GetPortfolio") },
            methodType = HttpMethod.Get
        ) {
            parameter("pagenumber", pageNumber)
            parameter("pageSize", pageSize)
            parameter("requestCode", requestCode)
            parameter("startDate", startDate)
            parameter("endDate", endDate)
            parameter("applicantId", applicantId)
            parameter("searchValue", searchValue)
        }
    }

    override suspend fun getPortfolioRequestTypes(): Either<Failure.NetworkFailure, PortfolioRequestTypesModelDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PortfolioFilterApi/GetPortfolioRequestTypes") },
            methodType = HttpMethod.Get
        )
    }

    override suspend fun modifyPortfolio(
        items: List<ModifyPortfolioInput>
    ): Either<Failure.NetworkFailure, List<ModifyPortfolioResponseDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PortfolioApi/ModifyPortfolio") },
            methodType = HttpMethod.Post
        ) {
            setBody(items)
        }
    }

    override suspend fun getPortfolioApplicantApi(): Either<Failure.NetworkFailure, List<PortfolioRequestTypesDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PortfolioApplicantApi") },
            methodType = HttpMethod.Get
        )
    }

}
