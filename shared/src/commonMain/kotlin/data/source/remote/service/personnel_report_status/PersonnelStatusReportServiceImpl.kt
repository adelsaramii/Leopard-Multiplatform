package com.attendance.leopard.data.source.remote.service.personnel_report_status

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.data.source.remote.service.personnel_report_status.PersonnelStatusReportService
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.PersonnelReportStatusDto
import com.attendance.leopard.data.source.remote.model.dto.PersonnelStatusReportTypeDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class PersonnelStatusReportServiceImpl constructor(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : PersonnelStatusReportService {

    override suspend fun getPersonnelStatusReportList(
        pageNumber: Int?,
        pageSize: Int?,
        status: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PersonnelReportStatusDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PresentAbsentApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
            parameter("searchValue", searchValue)
            parameter("status", status)
        }
    }

    override suspend fun getPersonnelStatusReportTypes(): Either<Failure.NetworkFailure, PersonnelStatusReportTypeDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/PresentAbsentCountApi") },
            methodType = HttpMethod.Get
        )
    }

}