package com.ramatonn.todo.ui.task_list

import com.ramatonn.todo.data.Task

sealed class TaskListEvent{
    data class DeleteTaskClick(val task: Task): TaskListEvent()
    data class OnCompletedChange(val task: Task, val isCompleted: Boolean): TaskListEvent()
    object OnUndoDeleteClick: TaskListEvent()
    data class OnTaskEditClick(val task: Task): TaskListEvent()
    object OnAddTaskClick: TaskListEvent()
}
