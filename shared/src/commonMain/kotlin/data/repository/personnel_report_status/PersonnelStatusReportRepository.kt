package com.attendace.leopard.data.repository.personnel_report_status

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.LeopardTabBarTypes
import com.attendace.leopard.data.model.PersonnelReportStatusModel
import kotlinx.coroutines.flow.Flow

interface PersonnelStatusReportRepository {

    suspend fun getPersonnelStatusReportList(
        pageNumber: Int?,
        pageSize: Int?,
        status: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PersonnelReportStatusModel>>

    suspend fun getPersonnelStatusReportTypes(): Either<Failure.NetworkFailure, List<LeopardTabBarTypes>>

    suspend fun getAccessToken(): Flow<String>
    suspend fun getBaseUrl(): Flow<String>

}