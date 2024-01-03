package com.attendace.leopard.data.source.remote.service.request

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.util.error.Failure
import com.attendance.leopard.data.model.AddBulkRequestInput
import com.attendance.leopard.data.source.remote.model.dto.AddRequestResponseDto
import com.attendance.leopard.data.source.remote.model.dto.FormItemDto
import com.attendance.leopard.data.source.remote.model.dto.RequestFormTypeDto
import com.attendance.leopard.data.source.remote.model.dto.RequestSelectComponentDto
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.path
import kotlinx.coroutines.flow.first

class RequestServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : RequestService {
    override suspend fun getRequestFormType(codeId: String?): Either<Failure.NetworkFailure, List<RequestFormTypeDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/RequestFormTypeApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("codeId", codeId)
        }
    }

    override suspend fun getRequestForm(
        formId: String,
        isBulk: Boolean
    ): Either<Failure.NetworkFailure, List<FormItemDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/RequestFormApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("formId", formId)
            parameter("isBulk", isBulk)
        }
    }

    override suspend fun getRequestDetail(docId: String): Either<Failure.NetworkFailure, List<FormItemDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/MobileRequestApi") },
            methodType = HttpMethod.Get
        ) {
            parameter("docId", docId)
        }
    }

    override suspend fun getSelectComponentData(
        url: String,
        pageSize: Int,
        pageNumber: Int,
        searchValue: String
    ): Either<Failure.NetworkFailure, List<RequestSelectComponentDto>> {

        var apiUrl = "${authSettings.getBaseUrl().first()}/NFS.Web/$url"
        apiUrl = apiUrl.replace("&searchValue=#SEARCH_VALUE#", "")
        apiUrl = apiUrl.replace("#PAGE_NUMBER#", pageNumber.toString())
        apiUrl = apiUrl.replace("#PAGE_SIZE#", pageSize.toString())


        return apiClient.makeRequest(
            urlBuilder = URLBuilder(apiUrl),
            methodType = HttpMethod.Get
        )
    }

    override suspend fun addRequest(
        formKey: String,
        data: MutableMap<String, String>
    ): Either<Failure.NetworkFailure, AddRequestResponseDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/MobileRequestApi/$formKey") },
            methodType = HttpMethod.Post
        ) {
            if (formKey == "b8e09aaf-9270-4bb9-bd14-cd072a043cd1") {
                data["147adb1e-f3b0-4122-9bfc-8e51ee032c12"] = "1"
            }
            setBody(data)
        }
    }

    override suspend fun addBulkRequest(
        formKey: String,
        data: AddBulkRequestInput
    ): Either<Failure.NetworkFailure, AddRequestResponseDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/BulkRequestApi/$formKey") },
            methodType = HttpMethod.Post
        ) {
            setBody(data)
        }
    }

    override suspend fun deleteRequest(id: String): Either<Failure.NetworkFailure, Boolean> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/DeleteRequestApi") },
            methodType = HttpMethod.Post
        ) {
            parameter("id", id)
        }
    }
}