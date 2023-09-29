package com.ramatonn.todo.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.compose.runtime.mutableStateOf
import androidx.core.app.NotificationCompat
import com.ramatonn.todo.R
import com.ramatonn.todo.util.ACTION_SERVICE_CANCEL
import com.ramatonn.todo.util.ACTION_SERVICE_PAUSE
import com.ramatonn.todo.util.ACTION_SERVICE_RESUME
import com.ramatonn.todo.util.ACTIVE_DURATION
import com.ramatonn.todo.util.CLOCK_STATE
import com.ramatonn.todo.util.REST_DURATION
import com.ramatonn.todo.util.ROUNDS
import com.ramatonn.todo.util.TIMER_NOTIFICATION_CHANNEL
import com.ramatonn.todo.util.TIMER_NOTIFICATION_CHANNEL_ID
import com.ramatonn.todo.util.TIMER_NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import java.time.Duration
import java.util.Timer
import javax.inject.Inject
import javax.inject.Named
import kotlin.concurrent.fixedRateTimer


@AndroidEntryPoint
class TimerService : Service() {

    @Inject
    @Named("Timer")
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManager

    private val binder: TimerBinder = TimerBinder()

    private lateinit var mp: MediaPlayer

    var activeTime = mutableStateOf(180000L)
    var restTime = mutableStateOf(60000L)
    var rounds = mutableStateOf(3)

    val activeTimeRemainingMillis = mutableStateOf(180000L)
    val restTimeRemainingMillis = mutableStateOf(60000L)
    val roundsRemaining = mutableStateOf(3)

    val isRest = mutableStateOf(false)

    val clockState = mutableStateOf(ClockState.STOPPED)

    override fun onBind(intent: Intent?): IBinder {
        return binder
    }

    private lateinit var timer: Timer

    override fun onCreate() {
        mp = MediaPlayer.create(applicationContext, R.raw.riff)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (clockState.value == ClockState.STOPPED) {
            activeTime.value =
                intent?.getLongExtra(ACTIVE_DURATION, activeTime.value) ?: activeTime.value
            restTime.value = intent?.getLongExtra(REST_DURATION, restTime.value) ?: restTime.value
            activeTime.value = intent?.getLongExtra(ROUNDS, activeTime.value) ?: activeTime.value
        }
        when (intent?.getStringExtra(CLOCK_STATE)) {
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
        startForeground(TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(TIMER_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            TIMER_NOTIFICATION_CHANNEL_ID,
            TIMER_NOTIFICATION_CHANNEL,
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
        timer.cancel()
        clockState.value = ClockState.STOPPED
    }


    private fun startClock() {
        if (clockState.value == ClockState.STOPPED) {
            restTimeRemainingMillis.value = restTime.value
            activeTimeRemainingMillis.value = activeTime.value
            roundsRemaining.value = rounds.value
        }

        clockState.value = ClockState.STARTED

        timer = fixedRateTimer(initialDelay = 10L, period = 10L) {
            if (!isRest.value) {
                activeTimeRemainingMillis.value -= 10L
                updateNotification(
                    millisToFormattedString(
                        activeTimeRemainingMillis.value
                    )
                )
                if(activeTimeRemainingMillis.value <= 0){
                    mp.start()
                    if (roundsRemaining.value > 1) {
                                roundsRemaining.value -= 1
                                timer.cancel()
                                isRest.value = !isRest.value
                                activeTimeRemainingMillis.value = activeTime.value
                                startClock()
                    }
                    else {
                        TimerServiceHelper.triggerForeGroundService(this@TimerService, ACTION_SERVICE_CANCEL)
                    }
                }
            } else {
                restTimeRemainingMillis.value -= 10L
                updateNotification(
                    millisToFormattedString(
                        restTimeRemainingMillis.value
                    )
                )
                if (restTimeRemainingMillis.value <= 0){
                    mp.start()
                    timer.cancel()
                    isRest.value = !isRest.value
                    restTimeRemainingMillis.value = restTime.value
                    startClock()
                }
            }
        }
    }

    private fun updateNotification(formattedString: String) {
        notificationManager.notify(
            TIMER_NOTIFICATION_ID,
            notificationBuilder.setContentText(formattedString).build()
        )
    }

    @SuppressLint("RestrictedApi")
    fun setResumeButton() {
        notificationBuilder.addAction(
            0, "Resume", StopwatchServiceHelper.resumePendingIntent(this)
        )
    }

    @SuppressLint("RestrictedApi")
    fun setPauseButton() {
        notificationBuilder.addAction(0, "Pause", StopwatchServiceHelper.pausePendingIntent(this))
    }

    @SuppressLint("RestrictedApi")
    fun setCancelButton() {
        notificationBuilder.addAction(
            0, "Cancel", StopwatchServiceHelper.cancelPendingIntent(this)
        )
    }


    inner class TimerBinder : Binder() {
        fun getService(): TimerService = this@TimerService
    }

    fun millisToFormattedString(millis: Long): String {
        val duration = Duration.ofMillis(millis)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val seconds = duration.seconds % 60

        return when (hours) {
            0L -> String.format("%02d:%02d", minutes, seconds)
            else -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }


    }
}