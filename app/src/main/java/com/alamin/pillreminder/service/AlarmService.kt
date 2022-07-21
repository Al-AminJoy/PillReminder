package com.alamin.pillreminder.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.alamin.pillreminder.model.data.Pill

private const val TAG = "AlarmService"
class AlarmService : Service() {

    init {
        Log.d(TAG, "Service Is Running")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val dataString =  intent?.getParcelableArrayListExtra<Pill>("EXTRA")
        dataString?.let {
            Log.d(TAG, dataString.toString())
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

}