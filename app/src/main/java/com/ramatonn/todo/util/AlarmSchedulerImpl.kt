package com.ramatonn.todo.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ramatonn.todo.data.Alert
import com.ramatonn.todo.receivers.AlarmReceiver
import java.time.LocalDate
import java.time.ZoneId

class AlarmSchedulerImpl(private val context: Context?) : AlarmScheduler {

    private lateinit var  alarmManager : AlarmManager

    override fun schedule(alert: Alert) {

        context?.let {
            alarmManager = context.getSystemService(AlarmManager::class.java)
        }


        val endTime =
            alert.endTime.toString()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(ALERT_NAME, alert.name)
            putExtra(ALERT_SOUND, alert.soundAddress)
            putExtra(ALERT_END_TIME, endTime)
            putExtra(ALERT_START_TIME, alert.startTime.toString())
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            alert.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val startTime =
            alert.startTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()
                .toEpochMilli()


        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            startTime,
            alert.period,
            pendingIntent
        )
    }
    override fun cancel(alert: Alert) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alert.id,
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}