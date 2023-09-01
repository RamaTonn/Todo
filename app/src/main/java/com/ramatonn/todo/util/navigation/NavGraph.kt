package com.ramatonn.todo.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramatonn.todo.ui.task_list.TaskListScreen
import com.ramatonn.todo.ui.timer.TimerPicker


@Composable
fun SetupNavGraph(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screen.TaskList.route){
        composable(route = Screen.TaskList.route){
            TaskListScreen()
        }
        composable(route = Screen.Timer.route){
            TimerPicker()
        }
    }
}