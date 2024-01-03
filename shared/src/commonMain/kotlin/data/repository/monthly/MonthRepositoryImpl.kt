package com.attendace.leopard.data.repository.monthly

import arrow.core.Either
import com.attendace.leopard.data.model.Day
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.SummaryCategory
import com.attendance.leopard.data.model.WorkPeriod
import com.attendance.leopard.data.source.remote.model.dto.toDay
import com.attendance.leopard.data.source.remote.model.dto.toSubordinate
import com.attendance.leopard.data.source.remote.model.dto.toSummaryCategory
import com.attendance.leopard.data.source.remote.model.dto.toWorkPeriod
import com.attendace.leopard.data.source.remote.service.monthly.MonthlyService
import com.attendace.leopard.util.error.Failure

class MonthRepositoryImpl(
    private val monthlyService: MonthlyService
) : MonthRepository {

    override suspend fun getMonthlySummary(
        personId: String?, workperiodId: String
    ): Either<Failure.NetworkFailure, SummaryCategory> {
        return monthlyService.getMonthlySummery(personId = personId, workperiodId = workperiodId)
            .map {
                it.toSummaryCategory()
            }
    }


    override suspend fun getWorkPeriods(): Either<Failure.NetworkFailure, List<WorkPeriod>> {
        return monthlyService.getWorkPeriods().map {
            it.map {
                it.toWorkPeriod()
            }
        }
    }

    override suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<Subordinate>> {
        return monthlyService.getSubordinates(searchValue, pageNumber, pageSize).map {
            it.map {
                it.toSubordinate()
            }
        }
    }

    override suspend fun getWorkperiodCalendar(
        personId: String?,
        workperiodId: String
    ): Either<Failure.NetworkFailure, List<Day>> {
        return monthlyService.getWorkperiodCalendar(
            personId = personId,
            workperiodId = workperiodId
        ).map {
            it.map {
                it.toDay()
            }
        }
    }


}