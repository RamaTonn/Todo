package com.ramatonn.todo.data.category

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Category(
    val title: String,
    val tasks: List<Int>,
    @PrimaryKey(autoGenerate = true) val id: Int
)
