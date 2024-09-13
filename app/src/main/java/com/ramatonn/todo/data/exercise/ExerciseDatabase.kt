package com.ramatonn.todo.data.exercise

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ramatonn.todo.data.Converters

@Database(version = 1, entities = [Exercise::class])
@TypeConverters(Converters::class)
abstract class ExerciseDatabase: RoomDatabase() {
    abstract val dao: ExerciseDAO
}