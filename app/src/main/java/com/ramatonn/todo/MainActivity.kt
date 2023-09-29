package com.ramatonn.todo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ramatonn.todo.service.StopwatchService
import com.ramatonn.todo.service.TimerService
import com.ramatonn.todo.ui.theme.ThemeToggleButton
import com.ramatonn.todo.ui.theme.TodoTheme
import com.ramatonn.todo.util.CLOCK_TYPE
import com.ramatonn.todo.util.navigation.MyDrawer
import com.ramatonn.todo.util.navigation.Screen
import com.ramatonn.todo.util.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var initialDestination by mutableStateOf(Screen.TaskList.route)
    private var clockType by mutableStateOf(-1)
    private var isStopwatchBound by mutableStateOf(false)
    private var isTimerBound by mutableStateOf(false)
    private lateinit var stopwatchService: StopwatchService
    private lateinit var timerService: TimerService

    private val stopwatchConnection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as StopwatchService.StopwatchBinder
            this@MainActivity.stopwatchService = binder.getService()
            isStopwatchBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isStopwatchBound = false
        }
    }

    private val timerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as TimerService.TimerBinder
            this@MainActivity.timerService = binder.getService()
            isTimerBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isTimerBound = false
        }
    }

    /*TODO only bind if destination or service is active else unbind*/

    @OptIn(ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        clockType = intent.getIntExtra(CLOCK_TYPE, -1)

        setContent {
            val isDarkTheme = remember {
                mutableStateOf(true)
            }
            val navController = rememberNavController()

            val pagerState = rememberPagerState()

            initialDestination = when (clockType) {
                0 -> {
                    Screen.Clock.route
                }

                1 -> {
                    Screen.Clock.route
                }

                else -> {
                    Screen.TaskList.route
                }
            }

            navController.addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.route == Screen.Clock.route) {
                    Intent(this, StopwatchService::class.java).also { intent ->
                        bindService(intent, stopwatchConnection, Context.BIND_AUTO_CREATE)
                    }
                    Intent(this, TimerService::class.java).also { intent ->
                        bindService(intent, timerConnection, Context.BIND_AUTO_CREATE)
                    }
                    /*when (pagerState.currentPage) {
                        0 -> {
                            Intent(this, StopwatchService::class.java).also { intent ->
                                bindService(intent, stopwatchConnection, Context.BIND_AUTO_CREATE)
                            }
                        }

                        1 -> {
                            Intent(this, TimerService::class.java).also { intent ->
                                bindService(intent, timerConnection, Context.BIND_AUTO_CREATE)
                            }
                        }
                    }*/
                }
            }
            TodoTheme(isDarkTheme = isDarkTheme.value) {
                MyDrawer(themeButton = {
                    ThemeToggleButton(isDarkTheme = isDarkTheme)
                }, content = {
                    SetupNavGraph(
                        navController,
                        if (isStopwatchBound) stopwatchService else null,
                        isStopwatchBound,
                        if (isTimerBound) timerService else null,
                        isTimerBound,
                        pagerState,
                        initialDestination,
                        clockType
                    )
                }, navController = navController
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        if (isStopwatchBound) {
            unbindService(stopwatchConnection)
        }
        isStopwatchBound = false
    }
}