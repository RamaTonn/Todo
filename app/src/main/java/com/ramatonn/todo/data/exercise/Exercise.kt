package com.ramatonn.todo.data.exercise

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ramatonn.todo.data.Weekday
import kotlinx.serialization.Serializable

@Entity
@Serializable
data class Exercise(
    val name: String,
    var max: Float,
    var maxUnit: WeightUnit,
    val day: Weekday,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

enum class WeightUnit{
    LB, KG
}