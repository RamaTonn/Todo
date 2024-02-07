package com.ramatonn.todo.util

import com.ramatonn.todo.data.Alert

interface AlarmScheduler {
    fun schedule(alert: Alert)
    fun cancel(alert: Alert)
}