package com.ramatonn.todo.util.navigation

sealed class Screen(val route: String){
    object TaskList: Screen("Task_List")
    object Alerts: Screen("Alerts")
    object Clock: Screen("Clock")
    object Exercises: Screen("Exercises")

}
