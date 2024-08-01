package com.example.myapplication


import android.app.Application
import android.content.IntentFilter

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val phoneStateReceiver = PhoneStateReceiver()
        val filter = IntentFilter()
        filter.addAction("android.intent.action.PHONE_STATE")
        registerReceiver(phoneStateReceiver, filter)
    }
}