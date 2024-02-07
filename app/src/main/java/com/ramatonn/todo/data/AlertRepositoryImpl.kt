package com.ramatonn.todo.data

import kotlinx.coroutines.flow.Flow

class AlertRepositoryImpl(private val dao: AlertDAO):AlertRepository {
    override suspend fun upsertAlert(alert: Alert) {
        dao.upsertAlert(alert)
    }

    override suspend fun deleteAlert(alert: Alert) {
        dao.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<Alert>> {
        return dao.getAlerts()
    }

    override fun getAlertById(id: Int): Alert? {
        return dao.getAlertById(id)
    }
}