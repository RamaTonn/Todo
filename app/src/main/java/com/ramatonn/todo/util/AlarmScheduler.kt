package com.ramatonn.todo.util

import com.ramatonn.todo.data.alert.Alert
import com.ramatonn.todo.data.task.Task

interface AlarmScheduler {
    fun scheduleAlert(alert: Alert)
    fun cancelAlert(alert: Alert)
    fun scheduleTask(task: Task)
    fun cancelTask(task: Task)
}