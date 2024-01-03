package com.attendace.leopard.data.source.remote.service.daily

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.AttendanceDto
import com.attendance.leopard.data.source.remote.model.dto.SummaryCategoryDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class DailyServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : DailyService {
    override suspend fun getDailySummery(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategoryDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/DailySummeryApi/GetMobileMonthlySummery") },
            methodType = HttpMethod.Get
        ) {
            parameter("date", date)
            parameter("personId", personId)
            parameter("workperiodId", workperiodId)
        }
    }

    override suspend fun getDailyAttendance(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<AttendanceDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/DailyAttendanceApi/GetWorkperiodCalendar") },
            methodType = HttpMethod.Get
        ) {
            parameter("date", date)
            parameter("personId", personId)
            parameter("workperiodId", workperiodId)
        }
    }
}
