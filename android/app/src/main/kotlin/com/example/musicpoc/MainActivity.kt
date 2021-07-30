package com.example.musicpoc

import io.flutter.embedding.android.FlutterActivity
import androidx.annotation.NonNull
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.BatteryManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.app.ActivityManager


class MainActivity : FlutterActivity() {
    private val CHANNEL = "battery"

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)
        MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        ).setMethodCallHandler { call, result ->
            if (call.method == "getBatteryLevel") {
                if (isMyServiceRunning(BackgroundSoundService::class.java)) {
                    val musicStarted = stopMusic()
                    result.success(musicStarted)
                } else {
                    val musicStarted = startMusic()
                    result.success(musicStarted)
                }
            } else {
                result.notImplemented()
            }
        }
    }

    private fun startMusic(): Boolean {
        try {
            val myService = Intent(this@MainActivity, BackgroundSoundService::class.java)
            startService(myService)
            return true
        } catch (e: Exception) {
            return false
        }

    }

    private fun stopMusic(): Boolean {
        try {
            val myService = Intent(this@MainActivity, BackgroundSoundService::class.java)
            stopService(myService)
            return true
        } catch (ex: Exception) {
            return false
        }

    }

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}
