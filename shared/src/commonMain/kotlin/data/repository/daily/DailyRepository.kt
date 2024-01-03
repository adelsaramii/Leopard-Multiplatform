package com.attendace.leopard.data.repository.daily

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.Attendance
import com.attendance.leopard.data.model.SummaryCategory

interface DailyRepository {
    suspend fun getDailySummery(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategory>

    suspend fun getDailyAttndance(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<Pair<Attendance, Attendance>>>
}