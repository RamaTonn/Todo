package com.ramatonn.todo.data.category

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ramatonn.todo.data.Converters


@Database(version = 1, entities = [Category::class])
@TypeConverters(Converters::class)
abstract class CategoryDatabase: RoomDatabase() {
    abstract val dao: CategoryDAO
}