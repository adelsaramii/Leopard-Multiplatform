package com.attendace.leopard.data.source.remote.service.salary

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.SalaryDto

interface SalaryService {

    suspend fun getSalary(personId: String?, workPeriodId: String):
            Either<Failure.NetworkFailure, SalaryDto>
}