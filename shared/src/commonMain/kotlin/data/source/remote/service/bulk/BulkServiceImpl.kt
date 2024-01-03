package com.attendance.leopard.data.source.remote.service.bulk

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.data.source.remote.service.bulk.BulkService
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.BulkDto
import com.attendance.leopard.data.source.remote.model.dto.WorkPeriodDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class BulkServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : BulkService {

    override suspend fun getMonthlyDetails(
        personId: String?,
        workPeriodId: String,
        codeId: String
    ): Either<Failure.NetworkFailure, List<BulkDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Mobile/api/MonthlyDetailsApi")
            }, methodType = HttpMethod.Get
        ) {
            parameter("personId", personId)
            parameter("workperiodId", workPeriodId)
            parameter("codeId", codeId)
        }
    }

    override suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriodDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Mobile/api/WorkPeriodApi/GetWorkPeriods")
            }, methodType = HttpMethod.Get
        )
    }
}
