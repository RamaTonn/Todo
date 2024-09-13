package com.ramatonn.todo.data.category

import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(category: Category)

    fun getCategories(): Flow<List<Category>>
}