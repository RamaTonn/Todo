package com.ramatonn.todo.data

import kotlinx.coroutines.flow.Flow

class CategoryRepositoryImpl(private val dao: CategoryDAO): CategoryRepository {

    override suspend fun upsertCategory(category: Category) {
        dao.upsertCategory(category)
    }

    override suspend fun deleteCategory(category: Category) {
        dao.deleteCategory(category)
    }

    override fun getCategories(): Flow<List<Category>> {
        return dao.getCategories()
    }
}