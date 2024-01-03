package com.attendace.leopard.data.source.remote.service.my_request

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.MyRequestTypesModelDto
import com.attendance.leopard.data.source.remote.model.dto.RequestDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class MyRequestServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : MyRequestService {
    override suspend fun getRequest(
        codeId: String?,
        startDate: String?,
        endDate: String?,
        status: String?,
        searchValue: String?,
        pageNumber: Int?,
        pageSize: Int?
    ): Either<Failure.NetworkFailure, List<RequestDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/MyRequestApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("codeId", codeId)
            parameter("startDate", startDate)
            parameter("endDate", endDate)
            parameter("status", status)
            parameter("searchValue", searchValue)
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
        }
    }

    override suspend fun getRequestTypes(): Either<Failure.NetworkFailure, MyRequestTypesModelDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/MyRequestCodesApi") },
            methodType = HttpMethod.Get
        )
    }

}
