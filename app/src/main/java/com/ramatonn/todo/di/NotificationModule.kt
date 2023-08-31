package com.ramatonn.todo.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
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

/*    @ServiceScoped
    @Provides
    @Named("Task")
    fun provideTaskNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, com.ramatonn.todo.util.TASK_NOTIFICATION_CHANNEL_ID)
    }*/
    @ServiceScoped
    @Provides
//    @Named("Clock")
    fun provideClockNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder{
        return NotificationCompat.Builder(context, com.ramatonn.todo.util.CLOCK_NOTIFICATION_CHANNEL_ID)
    }

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager {
        return context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

}
