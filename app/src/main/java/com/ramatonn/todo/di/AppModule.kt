package com.ramatonn.todo.di

import android.app.AlarmManager
import android.app.Application
import androidx.room.Room
import com.ramatonn.todo.data.alert.AlertDatabase
import com.ramatonn.todo.data.alert.AlertRepository
import com.ramatonn.todo.data.alert.AlertRepositoryImpl
import com.ramatonn.todo.data.category.CategoryDatabase
import com.ramatonn.todo.data.category.CategoryRepository
import com.ramatonn.todo.data.category.CategoryRepositoryImpl
import com.ramatonn.todo.data.exercise.ExerciseDatabase
import com.ramatonn.todo.data.exercise.ExerciseRepository
import com.ramatonn.todo.data.exercise.ExerciseRepositoryImpl
import com.ramatonn.todo.data.task.TaskDatabase
import com.ramatonn.todo.data.task.TaskRepository
import com.ramatonn.todo.data.task.TaskRepositoryImpl
import com.ramatonn.todo.util.AlarmScheduler
import com.ramatonn.todo.util.AlarmSchedulerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTaskDatabase(app: Application): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db").build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository {
        return TaskRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideCategoryDatabase(app: Application): CategoryDatabase {
        return Room.databaseBuilder(app, CategoryDatabase::class.java, "category_db").build()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: CategoryDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideAlertDatabase(app: Application): AlertDatabase {
        return Room.databaseBuilder(app, AlertDatabase::class.java, "alert_db").build()
    }

    @Provides
    @Singleton
    fun provideAlertRepository(db: AlertDatabase): AlertRepository {
        return AlertRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideExerciseDatabase(app: Application): ExerciseDatabase {
        return Room.databaseBuilder(app, ExerciseDatabase::class.java, "exercise_db").build()
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(db: ExerciseDatabase): ExerciseRepository {
        return ExerciseRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(app: Application): AlarmScheduler {

        val context = app.applicationContext

        val alarmManager = context.getSystemService(AlarmManager::class.java)

        return AlarmSchedulerImpl(context, alarmManager)
    }
}