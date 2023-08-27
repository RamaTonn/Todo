package com.ramatonn.todo.ui.task_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramatonn.todo.data.CategoryRepository
import com.ramatonn.todo.data.CategoryRepositoryImpl
import com.ramatonn.todo.data.Task
import com.ramatonn.todo.data.TaskRepository
import com.ramatonn.todo.data.TaskRepositoryImpl
import com.ramatonn.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskListViewModel @Inject constructor(private val taskRepository: TaskRepository/*, private val categoryRepository: CategoryRepository*/) : ViewModel() {

    /*val categories = categoryRepository.getCategories()*/

    val tasks = taskRepository.getTasks()

    private val _uiEvent = Channel<UiEvent>()

    val uiEvent = _uiEvent.receiveAsFlow()

    private var deletedTask: Task? = null

    fun onEvent(event: TaskListEvent) {
        when (event) {
            is TaskListEvent.DeleteTaskClick -> viewModelScope.launch {
                deletedTask = event.task
                taskRepository.deleteTask(event.task)
                _uiEvent.send(UiEvent.ShowSnackbar(message = "Task deleted", action = "Undo"))
            }

            is TaskListEvent.OnAddTaskClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OpenDialog(-1))
            }

            is TaskListEvent.OnCompletedChange -> viewModelScope.launch {
                taskRepository.upsertTask(event.task.copy(complete = event.isCompleted))
            }

            is TaskListEvent.OnTaskEditClick -> viewModelScope.launch {
                _uiEvent.send(UiEvent.OpenDialog(event.task.id))
            }

            TaskListEvent.OnUndoDeleteClick -> deletedTask?.let {
                viewModelScope.launch { taskRepository.upsertTask(it) }
            }
        }
    }
}