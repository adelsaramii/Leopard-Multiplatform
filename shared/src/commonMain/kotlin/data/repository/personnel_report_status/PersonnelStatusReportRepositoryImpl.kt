package com.attendace.leopard.data.repository.personnel_report_status

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.PersonnelReportStatusModel
import com.attendance.leopard.data.source.remote.model.dto.leopardTabBarTypesIdGenerator
import com.attendace.leopard.data.source.remote.service.personnel_report_status.PersonnelStatusReportService
import com.attendace.leopard.util.error.Failure
import kotlinx.coroutines.flow.Flow

class PersonnelStatusReportRepositoryImpl constructor(
    private val service: PersonnelStatusReportService,
    private val authSettings: AuthSettings
) : PersonnelStatusReportRepository {

    override suspend fun getPersonnelStatusReportList(
        pageNumber: Int?,
        pageSize: Int?,
        status: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PersonnelReportStatusModel>> {
        return service.getPersonnelStatusReportList(
            pageNumber = pageNumber,
            pageSize = pageSize,
            status = status,
            searchValue = searchValue
        ).map {
            it.map { personnelReportStatusDto -> personnelReportStatusDto.toModel() }
        }
    }

    override suspend fun getPersonnelStatusReportTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>> {
        return service.getPersonnelStatusReportTypes().map { modelDto ->
            leopardTabBarTypesIdGenerator(modelDto.items)
        }
    }

    override suspend fun getAccessToken(): Flow<String> {
        return authSettings.getAccessToken()
    }

    override suspend fun getBaseUrl(): Flow<String> {
        return authSettings.getBaseUrl()
    }

}