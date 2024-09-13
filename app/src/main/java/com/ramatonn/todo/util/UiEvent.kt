package com.ramatonn.todo.util

import com.ramatonn.todo.data.task.Task

sealed class UiEvent {

    object PopBackStack: UiEvent()
    data class OpenDialog(val task: Task?): UiEvent()
    data class ShowSnackbar(val message: String, val action: String? = null): UiEvent()
}