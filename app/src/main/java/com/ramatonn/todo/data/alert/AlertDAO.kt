package com.ramatonn.todo.data.alert

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDAO {
    @Upsert
    suspend fun upsertAlert(alert: Alert)

    @Delete
    suspend fun deleteAlert(alert: Alert)

    @Query("SELECT * FROM alert")
    fun getAlerts(): Flow<List<Alert>>

    @Query("SELECT * FROM alert WHERE id = :id")
    fun getAlertById(id: Int): Alert?
}