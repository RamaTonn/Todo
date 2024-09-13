package com.ramatonn.todo.data.alert

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ramatonn.todo.data.Converters

@Database(version = 1, entities = [Alert::class])
@TypeConverters(Converters::class)
abstract class AlertDatabase: RoomDatabase() {
    abstract val dao: AlertDAO
}