package com.example.myapplication

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import androidx.core.app.NotificationCompat

class OverlayService : Service() {
    companion object {
        const val NOTIFICATION_ID = 10
        const val CHANNEL_ID = "overlay_service_channel"
    }

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("사랑On")
                .setContentText("for jeonhwawang service")
                .build()
            Log.d("Test", "start foreground")
            startForeground(NOTIFICATION_ID, notification)
        }

//        // 포그라운드 서비스 알림 설정
//        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        val channelId = CHANNEL_ID
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Overlay Service Channel",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }

//        val notification = Notification.Builder(this, channelId)
//            .setContentTitle("Overlay Service")
//            .setContentText("Showing overlay")
//            .setSmallIcon(R.drawable.ic_notification) // 여기에 실제 아이콘 리소스를 사용하세요.
//            .build()
//        Log.d("MyTag", "before startForeground")
//        startForeground(NOTIFICATION_ID, notification)  // startForeground() 호출 시 ID는 1 이상이어야 합니다.
//        Log.d("MyTag", "after startForeground")

        // SYSTEM_ALERT_WINDOW 권한 체크 및 요청
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        // Inflate layout
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_layout, null)

        // WindowManager 설정
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FORMAT_CHANGED
        )

        // 위치와 크기 설정
        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 100

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        // 버튼 설정
        val openAppButton = overlayView.findViewById<Button>(R.id.openAppButton)
        openAppButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            windowManager.removeView(overlayView)
            stopForeground(true)
            stopSelf()
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            CHANNEL_ID,
            "MyApp notification",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.enableVibration(true)
        notificationChannel.description = "AppApp Tests"

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            notificationChannel)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}