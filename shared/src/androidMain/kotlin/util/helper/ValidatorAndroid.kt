package com.attendace.leopard.util.helper

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri

actual fun openUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context?.startActivity(browserIntent)
}

@SuppressLint("StaticFieldLeak")
var context : Context? = null