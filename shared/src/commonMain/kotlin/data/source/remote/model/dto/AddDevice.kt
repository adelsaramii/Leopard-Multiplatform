package com.attendace.leopard.data.source.remote.model.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class AddDeviceInput(
    @SerialName("Brand") val brand: String,
    @SerialName("AppVersion") val appVersion: String,
    @SerialName("IsActive") val isActive: Boolean,
    @SerialName("DeviceId") val deviceId: String,
    @SerialName("DeviceName") val deviceName: String,
    @SerialName("OperatingSystem") val operatingSystem: Int,
    @SerialName("DeviceToken") val deviceToken: String,
    @SerialName("OperatingSystemVersion") val operatingSystemVersion: String,
)


@kotlinx.serialization.Serializable
data class AddDeviceResponseDto(
    @SerialName("Success") val success: Boolean,
    @SerialName("Message") val message: String,
    @SerialName("StatusCode") val statusCode: Int,
    @SerialName("Data") val data: DeviceDataDto,
)


@kotlinx.serialization.Serializable
data class DeviceDataDto(
    @SerialName("Id") val Id: String,
)