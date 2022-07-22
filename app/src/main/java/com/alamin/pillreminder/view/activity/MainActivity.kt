package com.alamin.pillreminder.view.activity

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.alamin.pillreminder.R
import com.alamin.pillreminder.databinding.ActivityMainBinding
import com.alamin.pillreminder.model.data.Pill
import com.alamin.pillreminder.model.data.RecentSchedule
import com.alamin.pillreminder.utils.SetPillListener
import android.widget.Toast
import com.alamin.pillreminder.service.*
import kotlinx.android.synthetic.main.content_main.view.*


private const val TAG = "MainActivity"
const val NOTIFICATION_CHANNEL:String = "NOTIFICATION_CHANNEL"
class MainActivity : AppCompatActivity(), SetPillListener {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var pill: Pill? = null
    private var message: RecentSchedule? =null
    private var isImmediate: Boolean? = false
    private var pillList:List<Pill>? = null

    private var  intent: Intent? = null

    override fun onStart() {
        super.onStart()
        registerReceiver(broadcastReceiver, IntentFilter(ACTION_NOTIFICATION))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.includeContent.toolbar)

        val navController = findNavController(R.id.fragment)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.profileFragment,
                R.id.pillListFragment
            ), binding.layoutDrawer
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return super.onSupportNavigateUp() || navController.navigateUp(appBarConfiguration)
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL,
                "NOTIFICATION",
                importance)
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        val notification = getForegroundServiceNotificationBuilder()
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(999,notification)
    }

    private fun getForegroundServiceNotificationBuilder(): Notification {
        val messageTitle = if (isImmediate == true) "Medicine Reminder ${message?.pillName}" else "Upcoming Medicine ${message?.pillName}"
        val messageText = "${message?.unit} ${message?.pillUnit}"
        val mBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.capsule)
            .setContentTitle(messageTitle)
            .setContentText(messageText)
            .setAutoCancel(true)
            .setOngoing(true)
        val resultIntent = Intent(applicationContext, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(applicationContext, 0, resultIntent, 0)
        mBuilder.setContentIntent(resultPendingIntent)
        return mBuilder.build()
    }

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            message = p1?.getParcelableExtra(PILL_REMINDER)
            isImmediate = p1?.getBooleanExtra(REMIND_TYPE,false)
            createNotificationChannel()
        }
    }



    override fun onDestroy() {

        intent?.let {
            stopService(intent)
        }

        if (pillList == null){
            Toast.makeText(applicationContext, "Null from Activity", Toast.LENGTH_SHORT).show()

        }
        var broadcastIntent =  Intent()
        broadcastIntent.action = "restartservice"
        broadcastIntent.putParcelableArrayListExtra("PILL_DATA", ArrayList(pillList))
        broadcastIntent.setClass(this, Starter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }


    override fun onFindPill(pillList: List<Pill>) {
        this.pillList = pillList
        intent?.let {
            stopService(intent)
        }
        intent = Intent(this, AlarmService::class.java)
        intent?.putParcelableArrayListExtra("EXTRA", ArrayList(pillList))
        startService(intent)
    }
}