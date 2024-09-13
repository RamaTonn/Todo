package com.ramatonn.todo.ui.task_list

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramatonn.todo.data.task.Task
import com.ramatonn.todo.util.UiEvent
import com.ramatonn.todo.util.navigation.MyFAB

//import com.ramcosta.composedestinations.annotation.Destination
//import com.ramcosta.composedestinations.annotation.RootNavGraph

/*

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    var task by remember { mutableStateOf<Task?>(null) }

    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()

    val showBottomSheet = remember {
        mutableStateOf(false)
    }

    Scaffold(floatingActionButton = {
        MyFAB {
            task = null
            showBottomSheet.value = true
        }
    }, snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            items(tasks) { currTask ->
                TaskView(task = currTask, viewModel = viewModel, onclick = {
                    task = currTask
                    showBottomSheet.value = true
                })

            }
        }
    }

    TaskScreen(showBottomSheet = showBottomSheet, task = task, snackbarHostState = snackbarHostState, scope = scope)

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun ExpandableView(
        title: String,
        titleFontSize: TextUnit = MaterialTheme.typography.titleMedium.fontSize,
        titleFontWeight: FontWeight = FontWeight.Bold,
        content: @Composable () -> Unit
    ) {
        var expandedState by remember { mutableStateOf(false) }
        val rotationState by animateFloatAsState(
            targetValue = if (expandedState) 180f else 0f, label = ""
        )

        Card(modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300, easing = LinearOutSlowInEasing
                )
            ), shape = RoundedCornerShape(4.dp), onClick = {
            expandedState = !expandedState
        }) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(6f),
                        text = title,
                        fontSize = titleFontSize,
                        fontWeight = titleFontWeight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Icon(
                        modifier = Modifier
                            .weight(1f)
                            .rotate(rotationState),
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Drop-Down Arrow"
                    )

                }
                if (expandedState) {
                    content()
                }
            }
        }
    }
}

*/


@Composable
fun TaskListScreen(viewModel: TaskListViewModel = hiltViewModel()) {

    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    var task by remember {
        mutableStateOf<Task?>(null)
    }
    val isDialogOpen = remember {
        mutableStateOf(false)
    }

    val snackbarState = remember {
        SnackbarHostState()
    }

    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true){
        viewModel.uiEvent.collect {event ->
            when (event){
                is UiEvent.ShowSnackbar -> {
                    val result = snackbarState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )
                    if (result == SnackbarResult.ActionPerformed){
                        viewModel.onEvent(TaskListEvent.OnUndoDeleteClick)
                    }
                }
                is UiEvent.OpenDialog -> {
                    task = event.task
                    isDialogOpen.value = true
                    Log.i("Dialog", "TaskListScreen: Dialog Opened")
                }
                else -> Unit
            }
        }
    }

    Scaffold(floatingActionButton = {
        MyFAB {
            viewModel.onEvent(TaskListEvent.OnAddEditTaskClick(null))
        }
    }, snackbarHost = { SnackbarHost(hostState = snackbarState) }) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(top = paddingValues.calculateTopPadding())
        ) {
            itemsIndexed(tasks) {index, currTask ->
                TaskItem(task = currTask, onEvent = {event ->
                    viewModel.onEvent(event)
                })
                if (index < tasks.lastIndex){
                    Divider()
                }
            }
        }
    }

    TaskDialog(task = task, onEvent = {event -> viewModel.onEvent(event)}, isDialogOpen = isDialogOpen)
    /*TaskScreen(showBottomSheet = isDialogOpen, snackbarHostState = snackbarState, scope = scope, task = task)*/
}