package com.ramatonn.todo.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class ClockService: Service() {

    @Inject
//    @Named("Clock")
    lateinit var clockNotificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
