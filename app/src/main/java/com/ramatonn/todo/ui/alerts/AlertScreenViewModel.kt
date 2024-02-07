package com.ramatonn.todo.ui.alerts

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramatonn.todo.data.Alert
import com.ramatonn.todo.data.AlertRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertScreenViewModel @Inject constructor(
    private val alertRepository: AlertRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    val alerts = alertRepository.getAlerts()
    private var deletedAlert: Alert? = null

    var name by mutableStateOf("")
    var message by mutableStateOf("")
    var period by mutableStateOf(0L)
    var soundAddress by mutableStateOf<String?>(null)

    init {
        val alertId = savedStateHandle.get<Int>("alertId") ?: -1
        if (alertId != -1) {
            viewModelScope.launch {
                alertRepository.getAlertById(alertId)?.let {
                    name = it.name
                    message = it.message
                    period = it.period
                    soundAddress = it.soundAddress
                }
            }
        }
    }


    fun onSave(alert: Alert) {
        viewModelScope.launch {}
    }

    fun onDelete(alert: Alert) {
        viewModelScope.launch {
            deletedAlert = alert
            alertRepository.deleteAlert(alert)
        }
    }
}