package com.ramatonn.todo.ui.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramatonn.todo.data.task.Task
import com.ramatonn.todo.data.task.TaskRepository
import com.ramatonn.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(private val taskRepository: TaskRepository) : ViewModel() {

    val tasks = taskRepository.getTasks()

    private val _uiEvent = Channel<UiEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTask: Task? = taskRepository.getDeletedTask()

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.DeleteTaskClick -> viewModelScope.launch {
                deletedTask = event.task
                taskRepository.deleteTask(event.task)
                _uiEvent.send(UiEvent.ShowSnackbar(message = "Task deleted", action = "Undo"))
            }

            is TaskListEvent.OnCompletedChange -> viewModelScope.launch {
                taskRepository.upsertTask(event.task.copy(complete = event.isCompleted))

            }

            is TaskListEvent.OnFavouriteChange -> viewModelScope.launch {
                taskRepository.upsertTask(event.task.copy(favourite = event.isFavourite))
            }

            is TaskListEvent.OnAddEditTaskClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OpenDialog(event.task))
            }

            is TaskListEvent.OnUndoDeleteClick -> deletedTask?.let {
                viewModelScope.launch { taskRepository.upsertTask(it) }
            }

            is TaskListEvent.OnSaveClick -> viewModelScope.launch {
                taskRepository.upsertTask(event.task)
            }

        }
    }


}