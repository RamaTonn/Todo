package com.ramatonn.todo.data.exercise

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import io.ktor.util.date.WeekDay

@Dao
interface ExerciseDAO {

    @Upsert
    suspend fun upsertExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM exercise WHERE id = :id")
    suspend fun getExerciseByID(id: Int): Exercise?

    @Query("SELECT * FROM exercise WHERE day = :day")
    suspend fun getExercisesByDay(day: WeekDay): List<Exercise>
}