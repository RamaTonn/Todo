package com.ramatonn.todo.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class TimerService: Service() {

    @Inject
    @Named("Timer")
    lateinit var notificationBuilder: NotificationCompat.Builder
    @Inject
    lateinit var notificationManager: NotificationManager

    private val binder: TimerBinder = TimerBinder()

    val timeRemainingMillis = mutableStateOf(0L)

    val clockState = mutableStateOf(ClockState.STOPPED)

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    inner class TimerBinder: Binder(){
        fun getService(): TimerService = this@TimerService
    }
}