package com.attendace.leopard.data.repository.daily

import arrow.core.Either
import com.attendance.leopard.data.model.Attendance
import com.attendance.leopard.data.model.SummaryCategory
import com.attendance.leopard.data.source.remote.model.dto.toAttendance
import com.attendance.leopard.data.source.remote.model.dto.toSummaryCategory
import com.attendace.leopard.data.source.remote.service.daily.DailyService
import com.attendace.leopard.util.error.Failure

class DailyRepositoryImpl constructor(private val dailyService: DailyService) : DailyRepository {

    override suspend fun getDailySummery(
        date: String, personId: String?, workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategory> {
        return dailyService.getDailySummery(
            date = date,
            personId = personId,
            workperiodId = workperiodId
        )
            .map {
                it.toSummaryCategory()
            }
    }

    override suspend fun getDailyAttndance(
        date: String, personId: String?, workperiodId: String
    ): Either<Failure.NetworkFailure, List<Pair<Attendance, Attendance>>> {
        return dailyService.getDailyAttendance(
            date = date,
            personId = personId,
            workperiodId = workperiodId
        ).map {
            it.map { it.toAttendance() }
                .zipWithNext()
                .filterIndexed { index, _ ->
                    index % 2 == 0
                }
        }
    }


}