package com.attendace.leopard.data.source.remote.service.workplace

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto

interface WorkplaceService {
    suspend fun getWorkplaceChanges(modifiedSinceDate: String): Either<Failure.NetworkFailure, List<WorkplaceDto>>
}