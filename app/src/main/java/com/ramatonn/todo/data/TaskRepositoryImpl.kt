package com.ramatonn.todo.data

import kotlinx.coroutines.flow.Flow

class TaskRepositoryImpl(private val dao: TaskDAO): TaskRepository {

    override suspend fun upsertTask(task: Task) {
        dao.upsertTask(task)
    }

    override suspend fun deleteTask(task: Task) {
        dao.deleteTask(task)
    }

    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks()
    }

    override fun getTaskById(id: Int): Task?{
       return dao.getTaskById(id)
    }
}