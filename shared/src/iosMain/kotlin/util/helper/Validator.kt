package com.attendace.leopard.util.helper

import platform.UIKit.UIApplication
import platform.Foundation.NSURL

actual fun openUrl(url: String) {
    UIApplication.sharedApplication.openURL(NSURL(string = url))
}

