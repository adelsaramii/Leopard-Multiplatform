package com.attendace.leopard.data.repository.monthly

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.model.Day
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.SummaryCategory
import com.attendance.leopard.data.model.WorkPeriod

interface MonthRepository {
    suspend fun getMonthlySummary(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategory>

    suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriod>>


    suspend fun getWorkperiodCalendar(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<Day>>

    suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<Subordinate>>
}