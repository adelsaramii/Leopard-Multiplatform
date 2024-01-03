package com.attendace.leopard.data.repository.workplace

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.CommonFlow
import com.attendace.leopard.data.model.Workplace

interface WorkplaceRepository {
    suspend fun getWorkplaceChanges(): Either<Failure, Unit>
    fun getWorkplaces(): CommonFlow<List<Workplace>>
}