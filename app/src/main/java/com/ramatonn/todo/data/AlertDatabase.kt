package com.ramatonn.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(version = 1, entities = [Alert::class])
@TypeConverters(Converters::class)
abstract class AlertDatabase: RoomDatabase() {
    abstract val dao: AlertDAO
}