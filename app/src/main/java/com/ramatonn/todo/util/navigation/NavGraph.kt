package com.ramatonn.todo.util.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramatonn.todo.service.StopwatchService
import com.ramatonn.todo.ui.task_list.TaskListScreen
import com.ramatonn.todo.ui.timer.StopwatchView
import com.ramatonn.todo.ui.timer.TopNavigationClockView


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupNavGraph(navController: NavHostController, service: StopwatchService?, isBound: Boolean, pagerState: PagerState) {

    NavHost(
        navController = navController,
        startDestination = if (isBound) Screen.Clock.route else Screen.TaskList.route
    ) {
        composable(route = Screen.TaskList.route) {
            TaskListScreen()
        }
        composable(route = Screen.Clock.route) {
            val composables = listOf<@Composable () -> Unit>(
                { StopwatchView(service = service!!) },
                { /*TimerView(service = service!!)*/ }
            )
            val items = listOf("Stopwatch", "Timer")
            TopNavigationClockView(items, composables, pagerState, service!!)
        }
    }
}