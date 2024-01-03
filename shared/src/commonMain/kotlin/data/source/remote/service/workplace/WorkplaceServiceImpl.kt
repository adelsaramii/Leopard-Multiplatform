package com.attendace.leopard.data.source.remote.service.workplace

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.source.remote.model.dto.WorkplaceDto
import io.ktor.http.*
import kotlinx.coroutines.flow.first

class WorkplaceServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : WorkplaceService {

    override suspend fun getWorkplaceChanges(modifiedSinceDate: String): Either<Failure.NetworkFailure, List<WorkplaceDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first(),
            ).apply {
                path("NFS.Web/Default/Module.Avid/api/WorkplaceChangesApi")
            },
            toAppendHeaders = listOf(
                "If-Modified-Since" to modifiedSinceDate
            )
        )
    }
}