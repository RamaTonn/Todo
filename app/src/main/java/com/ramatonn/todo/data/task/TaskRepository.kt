package com.ramatonn.todo.data.task

import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun upsertTask(task: Task)

    suspend fun deleteTask(task: Task)

    fun getTasks(): Flow<List<Task>>

    fun getTaskById(id: Int): Task?

    fun getDeletedTask(): Task?
}