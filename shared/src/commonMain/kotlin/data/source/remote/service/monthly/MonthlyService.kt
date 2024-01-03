package com.attendace.leopard.data.source.remote.service.monthly

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.DayDto
import com.attendance.leopard.data.source.remote.model.dto.SubordinateDto
import com.attendance.leopard.data.source.remote.model.dto.SummaryCategoryDto
import com.attendance.leopard.data.source.remote.model.dto.WorkPeriodDto

interface MonthlyService {
    suspend fun getMonthlySummery(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategoryDto>

    suspend fun getWorkperiodCalendar(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<DayDto>>

    suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriodDto>>

    suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<SubordinateDto>>
}

