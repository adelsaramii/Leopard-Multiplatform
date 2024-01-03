package com.attendace.leopard.data.source.remote.service.attendance

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.AddLogResponseDto
import com.attendance.leopard.data.source.remote.model.dto.DayLogDto
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class AttendanceServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : AttendanceService {

    override suspend fun getTodayLogs(date: String): Either<Failure.NetworkFailure, DayLogDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {

                path("NFS.Web/Default/Module.Avid/api/TodayLogsApi")
            },
        ) {
            parameter("date", date)
        }
    }

    override suspend fun addLogs(
        isOffline: Boolean,
        logs: List<AddLogInput>
    ): Either<Failure.NetworkFailure, AddLogResponseDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Avid/api/LogApi")
            }, methodType = HttpMethod.Post
        ) {
            parameter("isOffline", isOffline)
            parameter("checkMissAttendance", false)
            setBody(logs)
        }
    }
}