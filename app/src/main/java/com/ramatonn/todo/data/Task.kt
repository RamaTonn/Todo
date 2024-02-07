package com.ramatonn.todo.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class Task(

    val title: String,
    var complete: Boolean = false,
    var favourite: Boolean = false,
    val startDate: LocalDate = LocalDate.now(),
    val nextDate: LocalDate = startDate,
    val deadlineDate: LocalDate? = null,
    val deadlineTime: LocalTime? = null,
    val repeatability: Repeatability = Repeatability(),
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)