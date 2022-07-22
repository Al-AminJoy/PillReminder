package com.alamin.pillreminder.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import com.alamin.pillreminder.model.data.Pill


class Starter : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val data =  intent?.getParcelableArrayListExtra<Pill>("PILL_DATA")

        Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show()
        var intent = Intent(context, AlarmService::class.java)
        intent.putParcelableArrayListExtra("EXTRA", ArrayList(data))
        context.startService(intent)
    }
}