package com.attendace.leopard.data.repository.bulk

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.model.Bulk
import com.attendance.leopard.data.model.WorkPeriod
import com.attendance.leopard.data.source.remote.model.dto.toBulk
import com.attendance.leopard.data.source.remote.model.dto.toWorkPeriod
import com.attendace.leopard.data.source.remote.service.bulk.BulkService
import com.attendace.leopard.util.error.Failure

class BulkRepositoryImpl(
    private val bulkService: BulkService,
    private val authSettings: AuthSettings
) : BulkRepository {

    override suspend fun getMonthlyDetails(
        personId: String?,
        workPeriodId: String,
        codeId: String
    ): Either<Failure.NetworkFailure, List<Bulk>> {
        return bulkService.getMonthlyDetails(
            personId = personId,
            workPeriodId = workPeriodId,
            codeId = codeId
        ).map {
            it.map { it.toBulk() }
        }
    }

    override suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriod>> {
        return bulkService.getWorkPeriods().map {
            it.map {
                it.toWorkPeriod()
            }
        }
    }

}
