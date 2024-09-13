package com.ramatonn.todo.util.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ramatonn.todo.service.StopwatchService
import com.ramatonn.todo.service.TimerService
import com.ramatonn.todo.ui.exercises.ExerciseScreen
import com.ramatonn.todo.ui.task_list.TaskListScreen
import com.ramatonn.todo.ui.timer.StopwatchView
import com.ramatonn.todo.ui.timer.TimerView
import com.ramatonn.todo.ui.timer.TopNavigationClockView


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    stopwatchService: StopwatchService?,
    isStopwatchBound: Boolean,
    timerService: TimerService?,
    isTimerBound: Boolean,
    pagerState: PagerState,
    initialDestination: String,
    clockType: Int
) {

    NavHost(
        navController = navController,
        startDestination = initialDestination
    ) {
        composable(route = Screen.TaskList.route) {
            TaskListScreen()
        }
        composable(route = Screen.Alerts.route){

        }
        composable(route = Screen.Clock.route) {
            val composables = listOf<@Composable () -> Unit>(
                {
                    if (stopwatchService != null) {
                        StopwatchView(service = stopwatchService)
                    }
                },
                {
                    if (timerService != null) {
                        TimerView(service = timerService)
                    }
                }
            )
            val items = listOf("Stopwatch", "Timer")
            if (isStopwatchBound || isTimerBound) {
                TopNavigationClockView(
                    items,
                    composables,
                    pagerState,
                    clockType
                )
            }
        }

        composable(route = Screen.Exercises.route) {
            ExerciseScreen()
        }
    }
}
