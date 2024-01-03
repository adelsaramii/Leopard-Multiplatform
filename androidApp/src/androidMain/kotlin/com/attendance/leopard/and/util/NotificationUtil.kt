package com.attendance.leopard.and.util

import android.content.Context
import android.os.Build
import com.attendace.leopard.presentation.screen.navigation.deviceInput
import com.attendace.leopard.data.source.remote.model.dto.AddDeviceInput
import com.attendace.leopard.util.device.getUniqueID
import com.attendace.leopard.util.platform.appVersionCode
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

fun notificationUtil(context: Context) {
    FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
        if (!task.isSuccessful) {
            return@OnCompleteListener
        }

        // Get new FCM registration token
        val token = task.result

        val addDeviceInput = AddDeviceInput(
            brand = Build.MANUFACTURER,
            appVersion = appVersionCode,
            deviceName = Build.MODEL,
            deviceId = getUniqueID(context),
            isActive = true,
            operatingSystem = 1,
            deviceToken = token,
            operatingSystemVersion = Build.VERSION.RELEASE
        )
        deviceInput.value = addDeviceInput
    })
}