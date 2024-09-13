package com.ramatonn.todo.ui.exercises

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramatonn.todo.data.Weekday
import com.ramatonn.todo.ui.CollapsibleCard

@Composable
fun ExerciseScreen(modifier: Modifier = Modifier, viewModel: ExerciseViewModel = hiltViewModel()) {
    val days = Weekday.values()
    LazyColumn {
        items(days) { day ->
            CollapsibleCard(title = { Text(text = day.toString()) }) {
                val exercises by viewModel.getExercisesByDay(day).collectAsState(initial = emptyList())
                Column {
                    exercises.forEach { exercise ->
                        Row {
                            Text(text = exercise.name)
                            Text(text = exercise.max.toString())
                            Text(text = exercise.maxUnit.toString())
                        }
                    }
                }
            }
        }
    }
}