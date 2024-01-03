package com.attendance.leopard.and.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.attendance.leopard.and.R
import org.json.JSONException

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val channelId = "leopard-channel"
    private val notifId = 0
    var id = 0
    private lateinit var summaryNotificationBuilder: NotificationCompat.Builder
    override fun onNewToken(s: String) {
        super.onNewToken(s)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        try {
            val params: Map<String?, String?> = remoteMessage.data
//        val data = JSONObject(params)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                createChannel(applicationContext)

            createNotification(
                applicationContext,
                NotificationData(
                    silent = params["silent"],
                    title = params["title"],
                    body = params["body"]
                )
            )
        } catch (e: JSONException) {
            Log.i("JSONException", "No valid json")
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun createChannel(context: Context) {
        val name = "Leopard"
        val description = "Leopard"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, name, importance)
        channel.description = description
        val systemService = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        systemService.createNotificationChannel(channel)
    }


    private fun createNotification(context: Context, data: NotificationData) {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val isNougatAndAbove = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
        summaryNotificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setGroup("LeopardGroup")
                .setGroupSummary(true)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_leopard_notif)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setSound(defaultSoundUri)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
        if (!isNougatAndAbove) {
            summaryNotificationBuilder.setGroupSummary(true)
        }
        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, channelId)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_leopard_notif)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        context.resources,
                        R.mipmap.ic_launcher
                    )
                )
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setGroup("LeopardGroup")

        if (!data.title.isNullOrEmpty() && !data.body.isNullOrEmpty()) {
            builder.setContentTitle(data.title)
            builder.setContentText(data.body)
            val bigText = NotificationCompat.BigTextStyle()
            bigText.setBigContentTitle(data.title)
            bigText.bigText(data.body)
            builder.setStyle(bigText)
        }
        val notificationManager = NotificationManagerCompat.from(context)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        notificationManager.notify(++id, builder.build())
        if (isNougatAndAbove) {
            notificationManager.notify(
                notifId /* ID of notification */,
                summaryNotificationBuilder.build()
            )
        }
    }


}


data class NotificationData(
    var silent: String? = null,
    var title: String? = null,
    var body: String? = null,
)