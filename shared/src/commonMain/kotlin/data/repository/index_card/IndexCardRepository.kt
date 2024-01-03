package com.attendace.leopard.data.repository.index_card

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.IndexCard

interface IndexCardRepository {

    suspend fun getIndexCards( personId: String?): Either<Failure.NetworkFailure, List<IndexCard>>

}