package com.attendace.leopard.util.notification

//import com.splendo.kaluga.base.utils.toByteArray
import platform.Foundation.NSData
import platform.Foundation.NSString
import platform.Foundation.stringWithFormat
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDelegateProtocol
import platform.UIKit.registerForRemoteNotifications
import platform.UserNotifications.UNNotification
import platform.UserNotifications.UNNotificationPresentationOptionAlert
import platform.UserNotifications.UNNotificationPresentationOptions
import platform.UserNotifications.UNNotificationResponse
import platform.UserNotifications.UNUserNotificationCenter
import platform.UserNotifications.UNUserNotificationCenterDelegateProtocol
import platform.darwin.NSObject

//class NotificationManagerDelegates(
//    private val onTokenGrant: (String) -> Unit,
//) : NSObject(), UIApplicationDelegateProtocol{
//
//    override fun application(
//        application: UIApplication,
//        didRegisterForRemoteNotificationsWithDeviceToken: NSData
//    ) {
//        try {
//            val tokenString = didRegisterForRemoteNotificationsWithDeviceToken
//                .toByteArray()
//                .joinToString("") {
//                    NSString.stringWithFormat("%02.2hhX", it)
//                }
//
//            onTokenGrant(tokenString)
//        } catch (e : Exception) {
//            onTokenGrant("")
//        }
//    }
//
//    override fun application(
//        application: UIApplication,
//        didFinishLaunchingWithOptions: Map<Any?, *>?
//    ): Boolean {
//        UIApplication.sharedApplication.registerForRemoteNotifications()
//        return true
//    }

//}