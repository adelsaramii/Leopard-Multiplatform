package com.attendace.leopard.data.source.remote.service.index_card

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.IndexCardDto

interface IndexCardService {

    suspend fun getIndexCard(
        personId: String?
    ): Either<Failure.NetworkFailure, List<IndexCardDto>>
}