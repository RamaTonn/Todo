package com.ramatonn.todo.util

sealed class UiEvent {

    object PopBackStack: UiEvent()
    data class Navigate(val route: String): UiEvent()
    data class OpenDialog(val id: Int): UiEvent()
    data class ShowSnackbar(val message: String, val action: String? = null): UiEvent()
}