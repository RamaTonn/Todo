package com.ramatonn.todo.util.navigation

sealed class Screen(val route: String){
    object TaskList: Screen("Task_List")
    object Timer: Screen("Timer")

}
