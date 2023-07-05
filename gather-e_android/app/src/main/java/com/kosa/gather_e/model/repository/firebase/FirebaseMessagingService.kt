package com.kosa.gather_e.model.repository.firebase

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kosa.gather_e.BottomNavigationVarActivity
import com.kosa.gather_e.R

class FirebaseMessagingService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        createNotificationChannelIfNeeded()

        val type = remoteMessage.data["type"]
            ?.let { NotificationType.valueOf(it) }
        val title = remoteMessage.data["title"]
        val message = remoteMessage.data["message"]
        Log.d("gather","title : ${title}")
        type ?: return

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
        NotificationManagerCompat.from(this)
            .notify(type.id, createNotification(type, title, message))
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = CHANNEL_DESCRIPTION
            // 설정
            channel.enableLights(true)  // 빛
            channel.enableVibration(true)   // 진동
            channel.lightColor = Color.RED
            channel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE

            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

    /*
val NOTIFICATION_ID = 1001;
createNotificationChannel(this, NotificationManagerCompat.IMPORTANCE_HIGH, false,
    getString(R.string.app_name), "App notification channel")   // 1

val channelId = "$packageName-${getString(R.string.app_name)}"
val title = "Android Developer"
val content = "Notifications in Android P"

val intent = Intent(baseContext, NewActivity::class.java)
intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
        intent, PendingIntent.FLAG_UPDATE_CURRENT)    // 2

val builder = NotificationCompat.Builder(this, channelId)
builder.setSmallIcon(R.drawable.ic_codechacha)
builder.setContentTitle(title)
builder.setContentText(content)
builder.priority = NotificationCompat.PRIORITY_HIGH   // 3
builder.setAutoCancel(true)
builder.setFullScreenIntent(fullScreenPendingIntent, true)   // 4

val notificationManager = NotificationManagerCompat.from(this)
notificationManager.notify(NOTIFICATION_ID, builder.build())
     */

    private fun createNotification(
        type: NotificationType,
        title: String?,
        message: String?
    ): Notification {
        val intent = Intent(this, BottomNavigationVarActivity::class.java).apply {
            putExtra("notificationType", "${type.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(this, type.id, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val fullScreenPendingIntent = PendingIntent.getActivity(baseContext, 0,
            intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setFullScreenIntent(fullScreenPendingIntent, true)
            .setAutoCancel(true)

        when (type) {
            NotificationType.NORMAL -> Unit
            NotificationType.EXPANDABLE -> {
//                notificationBuilder.setStyle(
//                    NotificationCompat.BigTextStyle()
//                        .bigText(
//                            "😀 😃 😄 😁 😆 😅 😂 🤣 🥲 ☺️ 😊 😇 " +
//                                    "🙂 🙃 😉 😌 😍 🥰 😘 😗 😙 😚 😋 😛 " +
//                                    "😝 😜 🤪 🤨 🧐 🤓 😎 🥸 🤩 🥳 😏 😒 " +
//                                    "😞 😔 😟 😕 🙁 ☹️ 😣 😖 😫 😩 🥺 😢 " +
//                                    "😭 😤 😠 😡 🤬 🤯 😳 🥵 🥶 😱 😨 😰 " +
//                                    "😥 😓 🤗 🤔 🤭 🤫 🤥 😶 😐 😑 😬 🙄 " +
//                                    "😯 😦 😧 😮 😲 🥱 😴 🤤 😪 😵 🤐 🥴 " +
//                                    "🤢 🤮 🤧 😷 🤒 🤕"
//                        )
//                )
            }
            NotificationType.CUSTOM -> {
//                notificationBuilder
//                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
//                    .setCustomContentView(
//                        RemoteViews(
//                            packageName,
//                            R.layout.view_custom_notification
//                        ).apply {
//                            setTextViewText(R.id.title, title)
//                            setTextViewText(R.id.message, message)
//                        }
//                    )
            }
        }

        return notificationBuilder.build()
    }

    companion object {
        private const val CHANNEL_NAME = "Gather&E Channel"
        private const val CHANNEL_DESCRIPTION = "Gather&E"
        private const val CHANNEL_ID = "gatherne"
    }

}
