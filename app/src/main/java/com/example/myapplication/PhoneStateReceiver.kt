package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.TelephonyManager
import android.util.Log

class PhoneStateReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
        val serviceIntent = Intent(context, OverlayService::class.java)

        when (state) {
            TelephonyManager.EXTRA_STATE_RINGING -> {
                Log.e("MY TAG","CALL RECEIVE")
                // 전화가 올 때 OverlayService 실행

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(serviceIntent)
                } else {
                    context.startService(serviceIntent)
                }
            }
            TelephonyManager.EXTRA_STATE_IDLE -> {
                Log.e("MY TAG","CALL END")
                // 전화가 끊겼을 때 OverlayService 중지
                context.stopService(serviceIntent)
            }
        }
    }
}