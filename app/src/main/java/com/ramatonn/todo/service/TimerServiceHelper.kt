package com.ramatonn.todo.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ramatonn.todo.MainActivity
import com.ramatonn.todo.util.ACTIVE_DURATION
import com.ramatonn.todo.util.CANCEL_REQUEST_CODE
import com.ramatonn.todo.util.CLICK_REQUEST_CODE
import com.ramatonn.todo.util.CLOCK_STATE
import com.ramatonn.todo.util.CLOCK_TYPE
import com.ramatonn.todo.util.PAUSE_REQUEST_CODE
import com.ramatonn.todo.util.REST_DURATION
import com.ramatonn.todo.util.RESUME_REQUEST_CODE
import com.ramatonn.todo.util.ROUNDS

object TimerServiceHelper {

    private const val flag = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(CLOCK_TYPE, 1)
        }
        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, intent, flag)
    }
    fun pausePendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra(CLOCK_STATE, ClockState.PAUSED)
        }
        return PendingIntent.getActivity(context, PAUSE_REQUEST_CODE, intent, flag)
    }
    fun resumePendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra(CLOCK_STATE, ClockState.STARTED)
        }
        return PendingIntent.getActivity(context, RESUME_REQUEST_CODE, intent, flag)
    }
    fun cancelPendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, TimerService::class.java).apply {
            putExtra(CLOCK_STATE, ClockState.STOPPED)
        }
        return PendingIntent.getActivity(context, CANCEL_REQUEST_CODE, intent, flag)
    }
    fun fullscreenPendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, TimerService::class.java)
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
    fun triggerForeGroundService(context: Context, action: String, active: Long? = null, rest: Long? = null, rounds: Int? = null){
        Intent(context, TimerService::class.java).apply {
            putExtra(ACTIVE_DURATION, active)
            putExtra(REST_DURATION, rest)
            putExtra(ROUNDS, rounds)
            this.action = action
            context.startService(this)
        }
    }
}