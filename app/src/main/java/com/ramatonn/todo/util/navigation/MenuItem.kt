package com.ramatonn.todo.util.navigation

import com.ramatonn.todo.R

sealed class MenuItem(val id:String, val text:String, val iconID: Int, val route: String){
    object TaskList: MenuItem("tasks", "Tasks", R.drawable.round_checklist_24, Screen.TaskList.route)
    object Timer: MenuItem("timer", "Timer", R.drawable.round_timer_24, Screen.Clock.route)
}
