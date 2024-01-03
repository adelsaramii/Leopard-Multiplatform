package com.attendace.leopard.data.repository.attendance

import arrow.core.Either
import com.attendace.leopard.data.source.remote.model.dto.AddLogInput
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.CommonFlow
import com.attendance.leopard.data.model.DayLog
import kotlinx.datetime.Instant

interface AttendanceRepository {
    suspend fun fetchTodayLogs(date: Instant): Either<Failure.NetworkFailure, DayLog>

    suspend fun getDayLogFlow(): Either<Failure.NetworkFailure, CommonFlow<DayLog>>
    suspend fun sendOnlineLog(log: AddLogInput):  Either<Failure, Unit>
    suspend fun sendOfflineSavedLogs()
}