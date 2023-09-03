package com.ramatonn.todo

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.ramatonn.todo.service.ClockService
import com.ramatonn.todo.ui.theme.ThemeToggleButton
import com.ramatonn.todo.ui.theme.TodoTheme
import com.ramatonn.todo.util.navigation.MyDrawer
import com.ramatonn.todo.util.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var isBound by mutableStateOf(false)
    private lateinit var service: ClockService
    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as ClockService.ClockBinder
            this@MainActivity.service = binder.getService()
            isBound = true
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }
    }

    override fun onStart() {
        super.onStart()
        Intent(this, ClockService::class.java).also { intent ->
            bindService(intent, connection, Context.BIND_AUTO_CREATE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isDarkTheme = remember {
                mutableStateOf(true)
            }
            val navController = rememberNavController()
            TodoTheme(isDarkTheme = isDarkTheme.value) {
                MyDrawer(
                    themeButton = {
                        ThemeToggleButton(isDarkTheme = isDarkTheme)
                    },
                    content = {
                        if(isBound){ SetupNavGraph(navController = navController, service, isBound) }
                        else { SetupNavGraph(navController, null, isBound) }
                    },
                    navController = navController
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        isBound = false
        unbindService(connection)
    }
}