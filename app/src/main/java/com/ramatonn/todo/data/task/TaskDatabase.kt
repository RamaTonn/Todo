package com.ramatonn.todo.data.task

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ramatonn.todo.data.Converters

@Database(version = 1, entities = [Task::class])
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDAO
}