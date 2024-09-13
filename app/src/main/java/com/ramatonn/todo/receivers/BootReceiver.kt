package com.ramatonn.todo.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.ramatonn.todo.data.alert.Alert
import com.ramatonn.todo.data.alert.AlertRepository
import com.ramatonn.todo.data.task.TaskRepository
import com.ramatonn.todo.util.AlarmScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver: BroadcastReceiver() {

    @Inject
    lateinit var alertRepository: AlertRepository

    @Inject
    lateinit var taskRepository: TaskRepository


    @Inject
    lateinit var  alarmScheduler: AlarmScheduler

    override fun onReceive(context: Context?, intent: Intent?) {
        intent ?: return

        if (intent.action == Intent.ACTION_BOOT_COMPLETED){
//            reschedule()
            alarmScheduler.scheduleAlert(
                Alert(
                    name ="H20 Reminder",
                    message = "Seek fluid intake",
                    period = 45 * 60 * 1000L,
                    startTime = LocalTime.of(10, 0),
                    endTime = LocalTime.of(23, 59),
                )
            )
            Log.d("Debug", "onReceive: System Booted")
        }
    }

    private fun reschedule(){
        CoroutineScope(Dispatchers.IO).launch {

            taskRepository.getTasks().collect{ list ->
                list.forEach { task ->
                    if (!task.complete){
                        alarmScheduler.scheduleTask(task)
                    }
                }
            }

            alertRepository.getAlerts().collect{ list ->
                list.forEach { alert ->
                    if (alert.enabled){
                        alarmScheduler.scheduleAlert(alert)
                    }
                }
            }

        }
    }

}