package com.attendace.leopard.data.repository.bulk

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.WorkPeriod

interface BulkRepository {

    suspend fun getMonthlyDetails(
        personId: String?,
        workPeriodId: String,
        codeId: String
    ): Either<Failure.NetworkFailure, List<Bulk>>

    suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriod>>

}
