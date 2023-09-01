package com.ramatonn.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.ramatonn.todo.ui.task_list.TaskListScreen
import com.ramatonn.todo.ui.theme.ThemeToggleButton
import com.ramatonn.todo.ui.theme.TodoTheme
import com.ramatonn.todo.util.navigation.MyDrawer
import com.ramatonn.todo.util.navigation.SetupNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
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
                        SetupNavGraph(navController = navController)
                    },
                    navController = navController)
            }
        }
    }
}