package com.ramatonn.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(version = 1, entities = [Category::class])
@TypeConverters(Converters::class)
abstract class CategoryDatabase: RoomDatabase() {
    abstract val dao: CategoryDAO
}