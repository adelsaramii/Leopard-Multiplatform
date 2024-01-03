package com.attendace.leopard.data.source.remote.service.bulk

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.BulkDto
import com.attendance.leopard.data.source.remote.model.dto.WorkPeriodDto


interface BulkService {

    suspend fun getMonthlyDetails(
        personId: String?,
        workPeriodId: String,
        codeId: String
    ): Either<Failure.NetworkFailure, List<BulkDto>>

    suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriodDto>>

}
