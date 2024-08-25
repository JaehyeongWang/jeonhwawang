package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.PixelFormat
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
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


        startForegroundService()
        if (Settings.canDrawOverlays(this)) {
            showOverlay()
        } else {
            // 권한이 없을 때 사용자에게 알림 및 설정 화면으로 이동
            Toast.makeText(this, "오버레이 권한이 필요합니다.", Toast.LENGTH_LONG).show()
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            stopSelf() // 서비스 종료
        }
    }


    private fun startForegroundService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("사랑On")
                .setContentText("for jeonhwawang service")
                .setSmallIcon(R.drawable.ic_notification) // 적절한 아이콘 추가
                .build()
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL)
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                CHANNEL_ID,
                "Overlay Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    private fun showOverlay() {
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        overlayView = inflater.inflate(R.layout.overlay_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        params.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
        params.y = 100

        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        windowManager.addView(overlayView, params)

        val openAppButton = overlayView.findViewById<Button>(R.id.openAppButton)
        openAppButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            removeOverlay()
        }
    }

    private fun removeOverlay() {
        if (::overlayView.isInitialized && ::windowManager.isInitialized) {
            try {
                // View가 현재 WindowManager에 첨부된 상태인지 확인
                if (overlayView.isAttachedToWindow) {
                    windowManager.removeView(overlayView)
                }
            } catch (e: IllegalArgumentException) {
                // 뷰가 이미 제거된 상태라면 이 예외가 발생할 수 있습니다.
                e.printStackTrace()
            }
        }
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        removeOverlay()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}