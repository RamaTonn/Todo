package com.ramatonn.todo.util.navigation

sealed class Screen(val route: String){
    object TaskList: Screen("Task_List")
    object Clock: Screen("Clock")

}
