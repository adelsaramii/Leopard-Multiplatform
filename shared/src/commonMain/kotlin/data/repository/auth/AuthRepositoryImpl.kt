package com.attendace.leopard.data.repository.auth

import arrow.core.Either
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.local.setting.AuthSettings
import com.attendace.leopard.data.source.remote.service.auth.AuthService
import com.attendace.leopard.util.helper.CommonFlow
import com.attendace.leopard.util.helper.asCommonFlow
import com.attendace.leopard.data.local.database.workplace.WorkplaceDao
import com.attendace.leopard.data.model.RolePermissionModel
import com.attendace.leopard.data.source.remote.model.dto.toInitialize
import com.attendance.leopard.data.model.ImageComponents
import com.attendance.leopard.data.model.Initialize
import com.attendance.leopard.data.model.Login
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import com.attendace.leopard.di.loge
import com.attendance.leopard.data.source.remote.model.dto.toLogin
import com.attendance.leopard.data.source.remote.model.dto.toSubordinate
import com.attendance.leopard.data.source.remote.model.dto.toUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class AuthRepositoryImpl(
    private val authService: AuthService,
    private val authSetting: AuthSettings,
    private val workplaceDao: WorkplaceDao,
) : AuthRepository {

    override suspend fun login(
        username: String,
        password: String
    ): Either<Failure, Login> {

        val result = authService.login(username, password).map { it.toLogin() }
        result.fold(ifRight = {
            authSetting.setAccessToken(it.access_token)
            authSetting.setRefreshToken(it.refresh_token)
            authService.changeUserLocale().fold(ifLeft = {

            }, ifRight = {

            })
        }, ifLeft = {

        })
        return result
    }

    override suspend fun getUserInfo(): Either<Failure, User> {
        val result = authService.getUserInfo()
        result.fold(ifRight = {
            authSetting.setLoggedIn(true)
            authSetting.setName("${it.firstName} ${it.lastName}")
            authSetting.setRoleName(it.roleName ?: "")
            authSetting.setHasSubordinate(it.hasSubordinate)
            authSetting.setPersonCode(it.code)
            authSetting.setPersonId(it.personId)
        }, ifLeft = {

        })
        return result.map { dto ->
            dto.toUser()
        }
    }

    override suspend fun getRolePermissionModel(): Either<Failure, RolePermissionModel> {
        return authService.getRolePermissionModel().map {
            it.rolePermissionDtoToRolePermissionModel()
        }
    }

    override fun getOfflineUserInfo(): CommonFlow<User> {
        return flow {
            val offlineUserInfo = User(
                fullName = authSetting.getName().first(),
                roleName = authSetting.getRoleName().first(),
                hasSubordinate = authSetting.getHasSubordinate().first(),
                code = authSetting.getPersonCode().first(),
                id = authSetting.getPersonId().first(),
                firstName = "",
                lastName = ""
            )
            emit(offlineUserInfo)
        }.asCommonFlow()
    }

    override suspend fun initialize(url: String): Either<Failure, Initialize> {
        var finalUrl = url
        if (!url.lowercase().startsWith("http")) {
            finalUrl = "http://$url"
        }
        val response = authService.initialize(finalUrl)
        if (response.isRight()) {
            authSetting.setBaseUrl(finalUrl)
        }
        return response.map { dto ->
            dto.toInitialize()
        }
    }

    override fun isLoggedIn(): CommonFlow<Boolean> {
        return authSetting.isLoggedIn().asCommonFlow()
    }

    override fun getBaseUrl(): Flow<String> {
        return authSetting.getBaseUrl()
    }

    override suspend fun getImageComponents(): ImageComponents {
        val baseUrl = authSetting.getBaseUrl().first()
        val token = authSetting.getAccessToken().first()
        val personId = authSetting.getPersonId().first()
        return  ImageComponents(baseUrl, token, personId)
    }

    override fun getAccessToken(): Flow<String> {
        return authSetting.getAccessToken()
    }

    override fun getLocale(): Flow<String> {
        return authSetting.getLocale()
    }

    override fun getLocaleName(): Flow<String> {
        return authSetting.getLocaleName()
    }

    override suspend fun setLocale(tag: String, name: String) {
        authSetting.setLocale(tag, name)
    }

    override suspend fun logout() {
        authSetting.setAccessToken("")
        authSetting.setRefreshToken("")
        authSetting.setLoggedIn(false)
        authSetting.setWorkplacesLastModifiedTime("")
        workplaceDao.deleteWorkplaces()
    }

    override suspend fun changeUserLocale() {
        authService.changeUserLocale().fold(ifLeft = {

        }, ifRight = {

        })
    }

    override suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<Subordinate>> {
        return authService.getSubordinates(searchValue, pageNumber, pageSize).map {
            it.map {
                it.toSubordinate()
            }
        }
    }

    override suspend fun addDeviceInfo(deviceInputDto: AddDeviceInput): Either<Failure.NetworkFailure, String> {
        if(deviceInputDto.deviceToken.isNotEmpty())
            authSetting.setDeviceToken(deviceInputDto.deviceToken)
        return authService.addDeviceInfo(deviceInputDto).map { it.data.Id }
    }

    override fun getDeviceToken(): Flow<String> {
        return authSetting.getDeviceToken()
    }

    override fun getAddDeviceInfoCalled(): Flow<Boolean> {
        return authSetting.getAddDeviceInfoCalled()
    }

    override suspend fun setAddDeviceInfoCalled(value: Boolean) {
        return authSetting.setAddDeviceInfoCalled(value)
    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Either<Failure.NetworkFailure, Unit> {
        return authService.changePassword(oldPassword , newPassword)
    }
}