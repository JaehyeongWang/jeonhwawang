package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.TelephonyManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var phoneStateReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("MyTag", "MainActivity call")

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

//    override fun onDestroy() {
//        super.onDestroy()
//        // 리시버 해제
//        unregisterReceiver(phoneStateReceiver)
//    }
}