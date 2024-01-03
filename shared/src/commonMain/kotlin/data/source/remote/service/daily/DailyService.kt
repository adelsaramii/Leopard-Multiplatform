package com.attendace.leopard.data.source.remote.service.daily

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.AttendanceDto
import com.attendance.leopard.data.source.remote.model.dto.SummaryCategoryDto

interface DailyService {

    suspend fun getDailySummery(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategoryDto>

    suspend fun getDailyAttendance(
        date: String,
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<AttendanceDto>>
}