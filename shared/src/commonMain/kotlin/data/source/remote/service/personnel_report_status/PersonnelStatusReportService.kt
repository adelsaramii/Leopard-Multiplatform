package com.attendace.leopard.data.source.remote.service.personnel_report_status

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.PersonnelReportStatusDto
import com.attendance.leopard.data.source.remote.model.dto.PersonnelStatusReportTypeDto

interface PersonnelStatusReportService {

    suspend fun getPersonnelStatusReportList(
        pageNumber: Int?,
        pageSize: Int?,
        status: String?,
        searchValue: String?
    ): Either<Failure.NetworkFailure, List<PersonnelReportStatusDto>>

    suspend fun getPersonnelStatusReportTypes(): Either<Failure.NetworkFailure, PersonnelStatusReportTypeDto>

}