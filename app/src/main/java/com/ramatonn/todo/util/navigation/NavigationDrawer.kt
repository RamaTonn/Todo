package com.ramatonn.todo.util.navigation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.ramatonn.todo.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerBody(
    items: List<MenuItem>,
    selectedItem: MutableState<MenuItem>,
    drawerState: DrawerState,
    scope: CoroutineScope,
    navController: NavHostController
) {

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(16.dp))
        items.forEach {

            val isSelected = it == selectedItem.value

            NavigationDrawerItem(modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = it.iconID),
                        contentDescription = null,
                    )
                },
                selected = isSelected,
                onClick = {
                    scope.launch { drawerState.close() }
                    selectedItem.value = it
                    navController.navigate(it.route)
                },
                label = { Text(text = it.text) })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(scrollBehavior: TopAppBarScrollBehavior, clicked: () -> Unit, themeButton: @Composable () -> Unit) {
    TopAppBar(
        title = {
            Row{
                Text(text = "Title")
                Spacer(modifier = Modifier.weight(1f))
                themeButton()
            }
                },
        navigationIcon = {
        IconButton(onClick = clicked) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
        }

    }, scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDrawer(content: @Composable () -> Unit, themeButton: @Composable (() -> Unit)? = null, navController: NavHostController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val items = listOf(MenuItem.TaskList,MenuItem.Timer)

    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        DrawerBody(
            items = items,
            selectedItem = selectedItem,
            drawerState = drawerState,
            scope = coroutineScope,
            navController = navController
        )
    }) {
        Scaffold(modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection), topBar = {
            MyTopAppBar(
                scrollBehavior = scrollBehavior,
                clicked = {
                coroutineScope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() }
            },
                themeButton = { themeButton?.invoke() })
        }) {
            Surface(modifier = Modifier.padding(it)) {
                content()
            }
        }
    }
}

@Composable
fun MyFAB(onClick: () -> Unit) {
    FloatingActionButton(onClick = { onClick() }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = null)
    }
}