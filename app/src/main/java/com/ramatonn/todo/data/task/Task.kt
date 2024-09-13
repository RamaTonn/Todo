package com.ramatonn.todo.data.task

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramatonn.todo.data.Weekday
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class Task(

    val title: String,
    var complete: Boolean = false,
    var favourite: Boolean = false,
    val date: LocalDate? = null,
    val time: LocalTime = LocalTime.now(),
    val repeatPattern: RepeatPattern? = null,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

sealed class RepeatPattern(val tag: String) {
    object Daily : RepeatPattern("DAILY")
    data class Weekly(val daysOfWeek: List<Weekday>) : RepeatPattern("WEEKLY")
    data class MonthlyOnDay(val dayOfMonth: Int) : RepeatPattern("MONTHLY_DAY")
    data class MonthlyOnSpecificWeek(val weekNumber: Int, val weekday: Weekday) : RepeatPattern("MONTHLY_WEEK")
    object Yearly : RepeatPattern("YEARLY")
}

