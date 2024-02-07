package com.ramatonn.todo.di

import android.app.Application
import androidx.room.Room
import com.ramatonn.todo.data.AlertDatabase
import com.ramatonn.todo.data.AlertRepository
import com.ramatonn.todo.data.AlertRepositoryImpl
import com.ramatonn.todo.data.CategoryDatabase
import com.ramatonn.todo.data.CategoryRepository
import com.ramatonn.todo.data.CategoryRepositoryImpl
import com.ramatonn.todo.data.TaskDatabase
import com.ramatonn.todo.data.TaskRepository
import com.ramatonn.todo.data.TaskRepositoryImpl
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
    fun provideTaskDatabase(app: Application): TaskDatabase{
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_db").build()
    }

    @Provides
    @Singleton
    fun provideTaskRepository(db: TaskDatabase): TaskRepository{
        return TaskRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideCategoryDatabase(app: Application): CategoryDatabase{
        return Room.databaseBuilder(app, CategoryDatabase::class.java, "category_db").build()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: CategoryDatabase): CategoryRepository{
        return CategoryRepositoryImpl(db.dao)
    }

    @Provides
    @Singleton
    fun provideAlertDatabase(app: Application): AlertDatabase{
        return Room.databaseBuilder(app, AlertDatabase::class.java, "alert_db").build()
    }

    @Provides
    @Singleton
    fun provideAlertRepository(db: AlertDatabase): AlertRepository {
        return AlertRepositoryImpl(db.dao)
    }
}