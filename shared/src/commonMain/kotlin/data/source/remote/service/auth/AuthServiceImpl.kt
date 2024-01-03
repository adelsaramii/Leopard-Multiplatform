package com.attendace.leopard.data.source.remote.service.auth

import arrow.core.Either
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.ApiClient
import com.attendace.leopard.data.source.remote.makeRequest
import com.attendace.leopard.data.source.remote.model.dto.InitializeDto
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceResponseDto
import com.attendance.leopard.data.source.remote.model.dto.LoginDto
import com.attendance.leopard.data.source.remote.model.dto.RolePermissionDto
import com.attendance.leopard.data.source.remote.model.dto.SubordinateDto
import com.attendance.leopard.data.source.remote.model.dto.UserDto
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.flow.first

class AuthServiceImpl(
    private val apiClient: ApiClient,
    private val authSettings: AuthSettings,
) : AuthService {

    override suspend fun initialize(url: String): Either<Failure.NetworkFailure, InitializeDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(url)
                .apply {
                    path("NFS.Web/Default/Module.Mobile/api/HealthApi/Check")
                }
        )
    }


    override suspend fun login(
        username: String,
        password: String
    ): Either<Failure.NetworkFailure, LoginDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/oauth/token") },
            methodType = HttpMethod.Post
        ) {
            setBody(FormDataContent(
                Parameters.build {
                    append("grant_type", "password")
                    append("client_id", "Leopard")
                    append("username", username)
                    append("password", password)
                }
            ))
        }
    }

    override suspend fun getUserInfo(
    ): Either<Failure.NetworkFailure, UserDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/ProfileApi/GetOnlineUserProfileInfo") },
        )
    }

    override suspend fun getRolePermissionModel(): Either<Failure.NetworkFailure, RolePermissionDto> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/CheckPermissionApi") },
        )
    }

    override suspend fun changeUserLocale(): Either<Failure.NetworkFailure, Unit> {
        val locale = authSettings.getLocale().first()
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/ProfileApi/ChangeUserLocale") },
            methodType = HttpMethod.Post
        ) {
            parameter("localeId", locale.toLocaleID())
        }
    }

    override suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<SubordinateDto>> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply {
                path("NFS.Web/Default/Module.Mobile/api/SubordinateApi/GetSubordinates")
            }, methodType = HttpMethod.Get
        ) {
            parameter("searchValue", searchValue)
            parameter("pageNumber", pageNumber)
            parameter("pageSize", pageSize)
        }
    }

    override suspend fun addDeviceInfo(deviceInput: AddDeviceInput): Either<Failure.NetworkFailure, AddDeviceResponseDto> {
        return apiClient.makeRequest(urlBuilder = URLBuilder(
            authSettings.getBaseUrl().first()
        ).apply {
            path("NFS.Web/Default/Module.BasicInformation/api/DeviceInfo")
        }, methodType = HttpMethod.Post
        ){
            setBody(deviceInput)
        }
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Either<Failure.NetworkFailure, Unit> {
        return apiClient.makeRequest(
            urlBuilder = URLBuilder(
                authSettings.getBaseUrl().first()
            ).apply { path("NFS.Web/Default/Module.Mobile/api/ChangePasswordApi") },
            methodType = HttpMethod.Post
        ) {
            parameter("oldPassword", oldPassword)
            parameter("newPassword", newPassword)
        }
    }

}


fun String.toLocaleID(): String {
    return when (this) {
        "Kurdish" -> "1104"
        "Arabic" -> "2049"
        else -> "1033"
    }
}