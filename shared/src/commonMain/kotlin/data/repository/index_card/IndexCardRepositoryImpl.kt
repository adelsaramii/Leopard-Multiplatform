package com.attendace.leopard.data.repository.index_card

import arrow.core.Either
import com.attendance.leopard.data.model.IndexCard
import com.attendance.leopard.data.source.remote.model.dto.toIndexCard
import com.attendace.leopard.data.source.remote.service.index_card.IndexCardService
import com.attendace.leopard.util.error.Failure

class IndexCardRepositoryImpl(
    private val indexCardService: IndexCardService
) : IndexCardRepository {
    override suspend fun getIndexCards(personId: String?): Either<Failure.NetworkFailure, List<IndexCard>> {
        return indexCardService.getIndexCard(personId).map { it.map { it.toIndexCard() } }
    }
}