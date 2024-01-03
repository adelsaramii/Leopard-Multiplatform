package com.attendace.leopard.data.source.remote.service.index_card

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.source.remote.model.dto.IndexCardDto
import io.ktor.client.request.parameter
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class IndexCardServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : IndexCardService {
    override suspend fun getIndexCard(personId: String?): Either<Failure.NetworkFailure, List<IndexCardDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/IndexCardApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("personId", personId)
        }
    }
}