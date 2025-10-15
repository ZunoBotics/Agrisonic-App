package com.agrisonic.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.agrisonic.app.data.local.AppDatabase
import com.agrisonic.app.data.local.PreferencesManager
import com.agrisonic.app.data.remote.RetrofitInstance

class AgrisonicApplication : Application() {

    lateinit var preferencesManager: PreferencesManager
    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        // Initialize PreferencesManager
        preferencesManager = PreferencesManager(this)

        // Initialize Retrofit
        RetrofitInstance.initialize(preferencesManager)

        // Initialize Database
        database = AppDatabase.getDatabase(this)

        // Create notification channel
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = "Agrisonic Notifications"
            val channelDescription = "Notifications for Agrisonic app"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        lateinit var instance: AgrisonicApplication
            private set
    }

    init {
        instance = this
    }
}
