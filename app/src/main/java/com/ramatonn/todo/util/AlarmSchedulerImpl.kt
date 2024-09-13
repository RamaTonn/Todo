package com.ramatonn.todo.util

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ramatonn.todo.data.alert.Alert
import com.ramatonn.todo.data.task.Task
import com.ramatonn.todo.receivers.AlertReceiver
import com.ramatonn.todo.receivers.TaskReceiver
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class AlarmSchedulerImpl @Inject constructor(private val context: Context?, private val alarmManager: AlarmManager) : AlarmScheduler {

    /*private lateinit var alarmManager: AlarmManager*/

    override fun scheduleAlert(alert: Alert) {

        val endTime = alert.endTime.toString()

        val intent = Intent(context, AlertReceiver::class.java).apply {
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
            AlarmManager.RTC_WAKEUP, startTime, alert.period, pendingIntent
        )
    }

    override fun cancelAlert(alert: Alert) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alert.id,
                Intent(context, AlertReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    override fun scheduleTask(task: Task) {
/*        context?.let {
            alarmManager = context.getSystemService(AlarmManager::class.java)
        }*/

        val title = task.title
        val date = task.date
        val time = task.time

        val intent = Intent(context, TaskReceiver::class.java).apply {
            putExtra(TASK_TITLE, title)
            putExtra(TASK_DATE, date)
            putExtra(TASK_TIME, time)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            task.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val ldt = time.atDate(date)

        val triggerTime = ldt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent
        )

    }

    override fun cancelTask(task: Task) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                task.id,
                Intent(context, TaskReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}