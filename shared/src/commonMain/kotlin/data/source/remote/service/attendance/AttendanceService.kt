package com.attendace.leopard.data.source.remote.service.attendance

import arrow.core.Either
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.*

interface AttendanceService {


    suspend fun getTodayLogs(date: String): Either<Failure.NetworkFailure, DayLogDto>

    suspend fun addLogs(
        isOffline: Boolean,
        logs: List<AddLogInput>
    ): Either<Failure.NetworkFailure, AddLogResponseDto>

}