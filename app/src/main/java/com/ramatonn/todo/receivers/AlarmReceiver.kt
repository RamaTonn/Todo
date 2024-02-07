package com.ramatonn.todo.receivers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import com.ramatonn.todo.R
import com.ramatonn.todo.util.ALERT_CHANNEL
import com.ramatonn.todo.util.ALERT_CHANNEL_ID
import com.ramatonn.todo.util.ALERT_END_TIME
import com.ramatonn.todo.util.ALERT_MESSAGE
import com.ramatonn.todo.util.ALERT_NAME
import com.ramatonn.todo.util.ALERT_SOUND
import com.ramatonn.todo.util.ALERT_START_TIME
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager

    lateinit var notificationBuilder: NotificationCompat.Builder

    lateinit var audioManager: AudioManager

    override fun onReceive(context: Context?, intent: Intent?) {

        val name = intent?.getStringExtra(ALERT_NAME) ?: return
        val endTime = LocalTime.parse(intent.getStringExtra(ALERT_END_TIME))
        val startTime = LocalTime.parse(intent.getStringExtra(ALERT_START_TIME))
        if (LocalTime.now(ZoneId.systemDefault()).isAfter(endTime) || startTime.isAfter(LocalTime.now(ZoneId.systemDefault()))) {
            Log.i("Alert", "Active: ${LocalTime.now(ZoneId.systemDefault()).isAfter(endTime)}, ${LocalTime.now(ZoneId.systemDefault())}, EndTime: $endTime")
            return
        }
        else {
            val say = intent.getStringExtra(ALERT_MESSAGE)
            val play = intent.getStringExtra(ALERT_SOUND)

            /*context?.let {
                audioManager = it.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            }

            val mp = MediaPlayer.create(
                context,
                Uri.parse(play),
                null,
                AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build(),
                audioManager.generateAudioSessionId()
            )
            mp.setOnCompletionListener {
                it.reset()
            }*/

            val channel = NotificationChannel(
                ALERT_CHANNEL_ID, ALERT_CHANNEL, NotificationManager.IMPORTANCE_HIGH
            )
            channel.setSound(
                 Uri.parse(play),
                 AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build()
            )
            channel.enableLights(true)
            channel.lightColor = 0x00FFFF
            channel.vibrationPattern = longArrayOf(0,68,10,68)

            context?.let {
                notificationBuilder =
                    NotificationCompat.Builder(it, ALERT_CHANNEL_ID).setContentTitle(name)
                        .setContentText(say).setSmallIcon(R.drawable.round_water_drop_24)
            }

            val notification = notificationBuilder.build()

            notificationManager.createNotificationChannel(channel)
            notificationManager.notify(5, notification)
//            mp.start()
            Log.i("Alert", "onReceive: $name, $play")
        }
    }
}
