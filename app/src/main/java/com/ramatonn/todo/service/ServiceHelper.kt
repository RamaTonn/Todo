package com.ramatonn.todo.service

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ramatonn.todo.MainActivity
import com.ramatonn.todo.util.CANCEL_REQUEST_CODE
import com.ramatonn.todo.util.CLICK_REQUEST_CODE
import com.ramatonn.todo.util.PAUSE_REQUEST_CODE
import com.ramatonn.todo.util.RESUME_REQUEST_CODE

object ServiceHelper {

    private const val flag = PendingIntent.FLAG_IMMUTABLE

    fun clickPendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, MainActivity::class.java)
        return PendingIntent.getActivity(context, CLICK_REQUEST_CODE, intent, flag)
    }
    fun pausePendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, ClockService::class.java)
        return PendingIntent.getActivity(context, PAUSE_REQUEST_CODE, intent, flag)
    }
    fun resumePendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, ClockService::class.java)
        return PendingIntent.getActivity(context, RESUME_REQUEST_CODE, intent, flag)
    }
    fun cancelPendingIntent(context: Context): PendingIntent{
        val intent = Intent(context, ClockService::class.java)
        return PendingIntent.getActivity(context, CANCEL_REQUEST_CODE, intent, flag)
    }
    fun triggerForeGroundService(context: Context, action: String){
        Intent(context, ClockService::class.java).apply {
            this.action = action
            context.startService(this)
        }
    }
}