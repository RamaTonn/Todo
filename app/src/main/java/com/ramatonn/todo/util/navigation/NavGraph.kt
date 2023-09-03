package com.ramatonn.todo.util.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramatonn.todo.service.ClockService
import com.ramatonn.todo.ui.task_list.TaskListScreen
import com.ramatonn.todo.ui.timer.ClockView
import com.ramatonn.todo.ui.timer.EmptyClockView


@Composable
fun SetupNavGraph(navController: NavHostController, service: ClockService?, isBound: Boolean) {

    NavHost(navController = navController, startDestination = if (isBound) Screen.Clock.route else Screen.TaskList.route){
        composable(route = Screen.TaskList.route){
            TaskListScreen()
        }
        composable(route = Screen.Clock.route){
            if(isBound){ ClockView(service = service!!) }
            else { EmptyClockView() }
        }
    }
}