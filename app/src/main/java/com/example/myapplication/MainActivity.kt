package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.os.PowerManager

class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSIONS = 1

    private lateinit var phoneStateReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MyTag", "MainActivity call")

        checkPermission()

        // SYSTEM_ALERT_WINDOW 권한 체크 및 요청
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        requestIgnoreBatteryOptimizations(this)

//        phoneStateReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
//                if (state == TelephonyManager.EXTRA_STATE_RINGING) {
//                    // 전화가 올 때 OverlayService 실행
//                    val serviceIntent = Intent(context, OverlayService::class.java)
//                    context.startService(serviceIntent)
//                }
//            }
//        }
//
//        // 리시버 등록
//        val filter = IntentFilter()
//        filter.addAction("android.intent.action.PHONE_STATE")
//        registerReceiver(phoneStateReceiver, filter)
    }

    private fun checkPermission() {
        var permission = mutableMapOf<String, String>()
        permission["phone"] = Manifest.permission.READ_PHONE_STATE
        permission["notification"] = Manifest.permission.POST_NOTIFICATIONS
//        permission["storageRead"] = Manifest.permission.READ_EXTERNAL_STORAGE

        // 현재 권한 상태 검사
        var denied = permission.count { ContextCompat.checkSelfPermission(this, it.value)  == PackageManager.PERMISSION_DENIED }

        // 마시멜로 버전 이후
        if(denied > 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permission.values.toTypedArray(), REQUEST_PERMISSIONS)
        }
    }

    fun requestIgnoreBatteryOptimizations(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val packageName = context.packageName

            if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
                intent.data = Uri.parse("package:$packageName")
                context.startActivity(intent)
            }
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        // 리시버 해제
//        unregisterReceiver(phoneStateReceiver)
//    }
}