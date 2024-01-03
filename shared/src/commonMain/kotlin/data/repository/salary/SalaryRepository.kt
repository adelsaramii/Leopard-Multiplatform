package com.attendace.leopard.data.repository.salary

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.Salary

interface SalaryRepository {

    suspend fun getSalary(personId: String?, workPeriodId: String):
            Either<Failure.NetworkFailure, Salary>
}