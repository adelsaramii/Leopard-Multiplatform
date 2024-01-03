package com.attendace.leopard.data.source.remote.service.auth

import arrow.core.Either
import com.attendace.leopard.data.source.remote.model.dto.InitializeDto
import com.attendace.leopard.util.error.Failure
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceResponseDto
import com.attendance.leopard.data.source.remote.model.dto.LoginDto
import com.attendance.leopard.data.source.remote.model.dto.RolePermissionDto
import com.attendance.leopard.data.source.remote.model.dto.SubordinateDto
import com.attendance.leopard.data.source.remote.model.dto.UserDto

interface AuthService {
    suspend fun initialize(url: String): Either<Failure.NetworkFailure, InitializeDto>
    suspend fun login(username: String, password: String): Either<Failure.NetworkFailure, LoginDto>
    suspend fun getUserInfo(): Either<Failure.NetworkFailure, UserDto>
    suspend fun getRolePermissionModel(): Either<Failure.NetworkFailure, RolePermissionDto>
    suspend fun changeUserLocale(): Either<Failure.NetworkFailure, Unit>
    suspend fun getSubordinates(
        searchValue: String,
        pageNumber: Int,
        pageSize: Int
    ): Either<Failure.NetworkFailure, List<SubordinateDto>>

    suspend fun addDeviceInfo(deviceInput: AddDeviceInput): Either<Failure.NetworkFailure, AddDeviceResponseDto>
    suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): Either<Failure.NetworkFailure, Unit>
}