package com.ramatonn.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(version = 1, entities = [Task::class])
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract val dao: TaskDAO
}