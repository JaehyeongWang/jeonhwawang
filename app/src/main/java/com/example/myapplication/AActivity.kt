package com.example.myapplication

import android.app.PictureInPictureParams
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class AActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_a)
        enterPipMode()
        // "앱 열기" 버튼을 누르면 MainActivity 실행
        findViewById<Button>(R.id.openAppButton).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

//    override fun onResume() {
//        super.onResume()
//        // PiP 모드 진입
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            enterPipMode()
//        }
//    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        // PiP 모드 진입
        enterPipMode()
    }

    private fun enterPipMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(9, 16) // 원하는 가로 세로 비율로 설정합니다.
            val pipParams = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(pipParams)
        }
    }

    override fun onPictureInPictureModeChanged(isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        // PiP 모드 상태 변화에 따른 UI 업데이트 등 작업
    }
}