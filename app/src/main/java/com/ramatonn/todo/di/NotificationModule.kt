package com.ramatonn.todo.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.ramatonn.todo.R
import com.ramatonn.todo.service.StopwatchServiceHelper
import com.ramatonn.todo.service.TimerServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped
import javax.inject.Named


@Module
@InstallIn(ServiceComponent::class)
object NotificationModule {

    @ServiceScoped
    @Provides
    @Named("Task")
    fun provideTaskNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, com.ramatonn.todo.util.TASK_NOTIFICATION_CHANNEL_ID)
    }

    @ServiceScoped
    @Provides
    @Named("Stopwatch")
    fun provideStopwatchNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, com.ramatonn.todo.util.STOPWATCH_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Stopwatch")
            .setContentText("00:00.00")
            .setSmallIcon(R.drawable.round_hourglass_empty_24)
            .setOngoing(true)
            .addAction(0, "Pause", StopwatchServiceHelper.pausePendingIntent(context))
            .addAction(0, "Cancel", StopwatchServiceHelper.cancelPendingIntent(context))
            .setContentIntent(StopwatchServiceHelper.clickPendingIntent(context))
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
    }

    @ServiceScoped
    @Provides
    @Named("Timer")
    fun provideTimerNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, com.ramatonn.todo.util.TIMER_NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Timer")
            .setContentText("00:00:00")
            .setSmallIcon(R.drawable.round_timer_24)
            .setOngoing(true)
            .addAction(0, "Pause", TimerServiceHelper.pausePendingIntent(context))
            .addAction(0, "Cancel", TimerServiceHelper.cancelPendingIntent(context))
            .setContentIntent(TimerServiceHelper.clickPendingIntent(context))
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}
