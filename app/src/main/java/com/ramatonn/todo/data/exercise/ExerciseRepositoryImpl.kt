package com.ramatonn.todo.data.exercise

import com.ramatonn.todo.data.Weekday

class ExerciseRepositoryImpl(private val dao: ExerciseDAO): ExerciseRepository {


    override suspend fun upsertExercise(exercise: Exercise) {
        dao.upsertExercise(exercise)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        dao.deleteExercise(exercise)
    }

    override suspend fun getExerciseByID(id: Int): Exercise? {
        return dao.getExerciseByID(id)
    }

    override suspend fun getExercisesByDay(day: Weekday): List<Exercise> {
        return dao.getExercisesByDay(day)
    }

}
