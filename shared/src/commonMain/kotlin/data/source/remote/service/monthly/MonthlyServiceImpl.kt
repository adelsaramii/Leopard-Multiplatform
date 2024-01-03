package com.attendace.leopard.data.source.remote.service.monthly

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.DayDto
import com.attendance.leopard.data.source.remote.model.dto.SubordinateDto
import com.attendance.leopard.data.source.remote.model.dto.SummaryCategoryDto
import com.attendance.leopard.data.source.remote.model.dto.WorkPeriodDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class MonthlyServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : MonthlyService {

    override suspend fun getMonthlySummery(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategoryDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/MonthlySummeryApi/GetMobileMonthlySummery") },
            methodType = HttpMethod.Get
        ) {
            parameter("personId", personId)
            parameter("workperiodId", workperiodId)
        }
    }

    override suspend fun getWorkperiodCalendar(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<DayDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Mobile/api/CalendarApi/GetWorkpersiodCalendar")
            }, methodType = HttpMethod.Get
        ) {
            parameter("personId", personId)
            parameter("workperiodId", workperiodId)
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


    override suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<SubordinateDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Mobile/api/SubordinateApi/GetSubordinates")
            }, methodType = HttpMethod.Get
        ){
            parameter("searchValue", searchValue)
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
        }
    }
}