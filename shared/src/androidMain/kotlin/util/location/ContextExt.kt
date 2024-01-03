package com.attendace.leopard.util.location

import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.provider.Settings

fun Context.isGpsEnabled(): Boolean {
    var isGpsEnabled: Boolean
    val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    try {
        val locMode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE)
        isGpsEnabled = locMode != 0
    } catch (e: Settings.SettingNotFoundException) {
        e.printStackTrace()
    }
    return isGpsEnabled
}