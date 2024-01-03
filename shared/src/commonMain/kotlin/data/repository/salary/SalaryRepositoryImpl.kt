package com.attendace.leopard.data.repository.salary

import arrow.core.Either
import com.attendance.leopard.data.model.Salary
import com.attendance.leopard.data.source.remote.model.dto.toSalary
import com.attendace.leopard.data.source.remote.service.salary.SalaryService
import com.attendace.leopard.util.error.Failure

class SalaryRepositoryImpl constructor(private val salaryService: SalaryService) :
    SalaryRepository {


    override suspend fun getSalary(
        personId: String?,
        workPeriodId: String
    ): Either<Failure.NetworkFailure, Salary> {
        return salaryService.getSalary(workPeriodId = workPeriodId, personId = personId)
            .map { it.toSalary() }
    }


}