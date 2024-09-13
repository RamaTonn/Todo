package com.ramatonn.todo.data.exercise

import com.ramatonn.todo.data.Weekday

interface ExerciseRepository {

    suspend fun upsertExercise(exercise: Exercise)

    suspend fun deleteExercise(exercise: Exercise)

    suspend fun getExerciseByID(id: Int): Exercise?

    suspend fun getExercisesByDay(day: Weekday): List<Exercise>
}