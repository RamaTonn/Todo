package com.ramatonn.todo.data

import kotlinx.coroutines.flow.Flow

interface AlertRepository {
    suspend fun upsertAlert(alert: Alert)

    suspend fun deleteAlert(alert: Alert)

    fun getAlerts(): Flow<List<Alert>>

    fun getAlertById(id: Int): Alert?
}