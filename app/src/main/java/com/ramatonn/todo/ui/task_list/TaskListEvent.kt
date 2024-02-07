package com.ramatonn.todo.ui.task_list

import com.ramatonn.todo.data.Task

sealed class TaskListEvent{
    data class DeleteTaskClick(val task: Task): TaskListEvent()
    data class OnCompletedChange(val task: Task, val isCompleted: Boolean): TaskListEvent()
    data class OnFavouriteChange(val task: Task, val isFavourite: Boolean): TaskListEvent()
    object OnUndoDeleteClick: TaskListEvent()
    data class OnAddEditTaskClick(val task: Task?): TaskListEvent()
}
