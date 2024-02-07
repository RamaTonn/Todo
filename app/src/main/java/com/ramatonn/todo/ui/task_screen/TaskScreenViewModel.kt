package com.ramatonn.todo.ui.task_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ramatonn.todo.data.Repeatability
import com.ramatonn.todo.data.Task
import com.ramatonn.todo.data.TaskRepository
import com.ramatonn.todo.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject


@HiltViewModel
class TaskScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    var task by mutableStateOf<Task?>(null)

    var title by mutableStateOf("")

    var deadlineDate by mutableStateOf<LocalDate?>(null)

    var deadlineTime by mutableStateOf<LocalTime?>(null)

    var repeatability by mutableStateOf(Repeatability())

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val taskId = savedStateHandle.get<Int>("taskId") ?: -1
        if (taskId != -1) {
            viewModelScope.launch {
                taskRepository.getTaskById(taskId)?.let {
                    title = it.title
                    deadlineDate = it.deadlineDate
                    repeatability = it.repeatability
                    task = it
                }
            }
        }
    }

    fun onSave() {
        viewModelScope.launch {
            task = task?.copy(title = title, deadlineDate = deadlineDate, deadlineTime = deadlineTime) ?: Task(title = title, deadlineDate = deadlineDate, deadlineTime = deadlineTime)
            taskRepository.upsertTask(task!!)
        }
    }

    fun onDelete() {
        viewModelScope.launch {
            taskRepository.deleteTask(task = task!!)
        }
    }

}