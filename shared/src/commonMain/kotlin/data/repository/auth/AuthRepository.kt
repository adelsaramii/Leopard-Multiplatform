package com.attendace.leopard.data.repository.auth

import arrow.core.Either
import com.attendace.leopard.data.model.RolePermissionModel
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.util.helper.CommonFlow
import com.attendance.leopard.data.model.ImageComponents
import com.attendance.leopard.data.model.Initialize
import com.attendance.leopard.data.model.Login
import com.attendance.leopard.data.model.Subordinate
import com.attendance.leopard.data.model.User
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun initialize(url: String): Either<Failure, Initialize>
    fun isLoggedIn(): CommonFlow<Boolean>

    suspend fun login(username: String, password: String): Either<Failure, Login>
    suspend fun getUserInfo(): Either<Failure, User>
    suspend fun getRolePermissionModel(): Either<Failure, RolePermissionModel>
    fun getBaseUrl(): Flow<String>
    suspend fun getImageComponents(): ImageComponents

    fun getAccessToken(): Flow<String>

    fun getLocale(): Flow<String>
    suspend fun setLocale(tag: String, name: String)
    fun getLocaleName(): Flow<String>
    suspend fun logout()
    fun getOfflineUserInfo(): CommonFlow<User>
    suspend fun changeUserLocale()
    suspend fun getSubordinates(
        searchValue: String, pageNumber: Int, pageSize: Int
    ): Either<Failure.NetworkFailure, List<Subordinate>>

    suspend fun addDeviceInfo(deviceInputDto: AddDeviceInput): Either<Failure.NetworkFailure, String>
    fun getDeviceToken(): Flow<String>
    fun getAddDeviceInfoCalled(): Flow<Boolean>
    suspend fun setAddDeviceInfoCalled(value: Boolean)
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Either<Failure.NetworkFailure, Unit>
}