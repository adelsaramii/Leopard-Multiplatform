package com.attendace.leopard.data.source.remote.service.salary

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.SalaryDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class SalaryServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings
) : SalaryService {
    override suspend fun getSalary(
        personId: String?,
        workPeriodId: String
    ): Either<Failure.NetworkFailure, SalaryDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PayrollApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("workPeriodId", workPeriodId)
            parameter("personId", personId)
        }
    }
}