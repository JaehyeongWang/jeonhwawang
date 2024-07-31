package com.example.myapplication

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat

class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                // 전화가 올 때 알림을 생성하여 AActivity를 시작
                showNotification(context)
            }
        }
    }

    private fun showNotification(context: Context) {
        val activityIntent = Intent(context, AActivity::class.java)
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context,
            0,
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(context, "CALL_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("사랑On")
            .setContentText("고객정보 간단하게 보기")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(1, notification)
    }
}