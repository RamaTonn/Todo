package com.ramatonn.todo.ui.exercises

import androidx.lifecycle.ViewModel
import com.ramatonn.todo.data.Weekday
import com.ramatonn.todo.data.exercise.Exercise
import com.ramatonn.todo.data.exercise.ExerciseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


@HiltViewModel
class ExerciseViewModel @Inject constructor(private val repository: ExerciseRepository): ViewModel() {

    fun getExercisesByDay(day: Weekday): Flow<List<Exercise>> {
        return flow {
            val exercises = repository.getExercisesByDay(day)
            emit(exercises)
        }
    }
}