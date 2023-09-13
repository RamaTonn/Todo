package com.ramatonn.todo.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.ramatonn.todo.util.ACTION_SERVICE_CANCEL
import com.ramatonn.todo.util.ACTION_SERVICE_PAUSE
import com.ramatonn.todo.util.ACTION_SERVICE_RESUME
import com.ramatonn.todo.util.CLOCK_STATE
import com.ramatonn.todo.util.STOPWATCH_NOTIFICATION_CHANNEL
import com.ramatonn.todo.util.STOPWATCH_NOTIFICATION_CHANNEL_ID
import com.ramatonn.todo.util.STOPWATCH_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.Timer
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.fixedRateTimer

@AndroidEntryPoint
class StopwatchService : Service() {

    @Inject
    @Named("Stopwatch")
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private val binder: StopwatchBinder = StopwatchBinder()

    val timePassedMillis = mutableStateOf(0L)

    val clockState = mutableStateOf(ClockState.STOPPED)

    private lateinit var timer: Timer

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.getStringExtra(CLOCK_STATE)){
            ClockState.STARTED.toString() -> {
                setPauseButton()
                startForegroundService()
                startClock()
            }
            ClockState.PAUSED.toString() -> {
                pauseClock()
                setResumeButton()
            }
            ClockState.STOPPED.toString() -> {
                pauseClock()
                cancelClock()
                stopForegroundService()
            }
        }
        intent?.action.let {
            when (it) {
                ACTION_SERVICE_RESUME -> {
                    notificationBuilder.clearActions()
                    setPauseButton()
                    setCancelButton()
                    startForegroundService()
                    startClock()
                }

                ACTION_SERVICE_PAUSE -> {
                    notificationBuilder.clearActions()
                    pauseClock()
                    setResumeButton()
                    setCancelButton()
                }

                ACTION_SERVICE_CANCEL -> {
                    pauseClock()
                    cancelClock()
                    stopForegroundService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        createNotificationChannel()
        startForeground(STOPWATCH_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(STOPWATCH_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            STOPWATCH_NOTIFICATION_CHANNEL_ID,
            STOPWATCH_NOTIFICATION_CHANNEL,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)

    }

    private fun pauseClock() {
        if (this::timer.isInitialized) {
            timer.cancel()
        }
        clockState.value = ClockState.PAUSED
    }

    private fun cancelClock() {
        timePassedMillis.value = 0
        clockState.value = ClockState.STOPPED
    }

    fun startClock() {
        clockState.value = ClockState.STARTED
        timer = fixedRateTimer(initialDelay = 10L, period = 10L) {
            timePassedMillis.value += 10
            updateNotification(
                millisToFormattedString(
                    timePassedMillis.value
                )
            )
        }
    }

    private fun updateNotification(formattedString: String) {
        notificationManager.notify(
            STOPWATCH_NOTIFICATION_ID,
            notificationBuilder.setContentText(formattedString).build()
        )
    }

    @SuppressLint("RestrictedApi")
    fun setResumeButton() {
        notificationBuilder.addAction(0, "Resume", ServiceHelper.resumePendingIntent(this)
        )
    }

    @SuppressLint("RestrictedApi")
    fun setPauseButton() {
        notificationBuilder.addAction(0, "Pause", ServiceHelper.pausePendingIntent(this))
    }

    @SuppressLint("RestrictedApi")
    fun setCancelButton() {
        notificationBuilder.addAction(0, "Cancel", ServiceHelper.cancelPendingIntent(this)
        )
    }

    inner class StopwatchBinder : Binder() {
        fun getService(): StopwatchService = this@StopwatchService
    }

    fun millisToFormattedString(millis: Long): String {
        val duration = Duration.ofMillis(millis)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60
        val milliSeconds =
            duration.minusHours(hours).minusMinutes(minutes).minusSeconds(seconds).toMillis()

        /*return when (clockType) {
            ClockType.STOPWATCH -> {
                when (hours) {
                    0L -> String.format("%02d:%02d.%02d", minutes, seconds, milliSeconds)
                    else -> String.format(
                        "%02d:%02d:%02d.%02d",
                        hours,
                        minutes,
                        seconds,
                        milliSeconds
                    )
                }
            }

            ClockType.TIMER -> {
                when (hours) {
                    0L -> String.format("%02d:%02d", minutes, seconds)
                    else -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
                }
            }
        }*/

        return when (hours) {
            0L -> String.format("%02d:%02d.%02d", minutes, seconds, milliSeconds/10)
            else -> String.format(
                "%02d:%02d:%02d.%02d",
                hours,
                minutes,
                seconds,
                milliSeconds/10
            )
        }
    }
}
