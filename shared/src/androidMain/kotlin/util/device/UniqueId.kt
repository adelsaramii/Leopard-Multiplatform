package com.attendace.leopard.util.device

import android.content.Context
import android.os.Build
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID
import android.provider.Settings

fun getUniqueID(context: Context): String {
    val m_szDevIDShort =
        "35" + Build.BOARD.length.rem(10)+ Build.BRAND.length % 10 + Build.CPU_ABI.length % 10 + Build.DEVICE.length % 10 + Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
    var serial: String? = null
    try {
        serial = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)
        return md5(UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString())
    } catch (exception: Exception) {
        serial = "serial" // some value
    }
    return md5(UUID(m_szDevIDShort.hashCode().toLong(), serial.hashCode().toLong()).toString())
}

fun md5(s: String): String {
    val MD5 = "MD5"
    try {
        // Create MD5 Hash
        val digest = MessageDigest
            .getInstance(MD5)
        digest.update(s.toByteArray())
        val messageDigest = digest.digest()

        // Create Hex String
        val hexString = StringBuilder()
        for (aMessageDigest in messageDigest) {
            var h = Integer.toHexString(0xFF and aMessageDigest.toInt())
            while (h.length < 2) h = "0$h"
            hexString.append(h)
        }
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
    }
    return ""
}