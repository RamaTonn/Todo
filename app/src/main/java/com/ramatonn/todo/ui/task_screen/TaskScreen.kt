package com.ramatonn.todo.ui.task_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramatonn.todo.R
import com.ramatonn.todo.data.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    viewModel: TaskScreenViewModel = hiltViewModel(),
    showBottomSheet: MutableState<Boolean>,
    task: Task? = null,
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope
) {

    viewModel.task = task

    var showDateDialog by remember {
        mutableStateOf(false)
    }

    var showTimeDialog by remember {
        mutableStateOf(false)
    }

    var showRepeatDialog by remember {
        mutableStateOf(false)
    }

    if (showBottomSheet.value) {
        var title by remember {
            mutableStateOf(task?.title ?: "")
        }

        var deadlineDate by remember {
            mutableStateOf(task?.deadlineDate)
        }

        var deadlineTime by remember {
            mutableStateOf(task?.deadlineTime)
        }

        var startDate by remember {
            mutableStateOf(task?.startDate)
        }

        val datePickerState = rememberDatePickerState()

        val timePickerState = rememberTimePickerState()

        ModalBottomSheet(
            onDismissRequest = { showBottomSheet.value = false }, dragHandle = null
        ) {
            val focusManager = LocalFocusManager.current
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = {
                        Text(text = "Title")
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus(true)
                    }),
                    maxLines = 1
                )
                Row(modifier = Modifier.padding(4.dp), verticalAlignment = Alignment.CenterVertically) {
                    when (deadlineDate) {
                        null -> IconButton(onClick = { showDateDialog = true }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.round_access_time_24),
                                contentDescription = "Deadline"
                            )
                        }

                        else -> Card(modifier = Modifier.padding(2.dp), shape = MaterialTheme.shapes.small) {
                            Text(text = deadlineDate!!.format(DateTimeFormatter.ofPattern("d, MMM")), modifier = Modifier.padding(2.dp))
                        }
                    }
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.round_repeat_24),
                            contentDescription = "Repeat"
                        )
                    }
                    if (task != null) {
                        IconButton(onClick = {
                            viewModel.onDelete()
                            showBottomSheet.value = false
                            scope.launch {
                                val result = snackbarHostState.showSnackbar(
                                    message = "Undo deleted task",
                                    actionLabel = "Undo",
                                    duration = SnackbarDuration.Short
                                )
                                when (result) {
                                    SnackbarResult.ActionPerformed -> {
                                        viewModel.onSave()
                                    }

                                    SnackbarResult.Dismissed -> {}
                                }
                            }
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.round_delete_24),
                                contentDescription = "Delete",
                                tint = Color(0xFFD34936)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(modifier = Modifier.padding(horizontal = 8.dp),
                        enabled = title.isNotBlank(),
                        onClick = {
                            viewModel.title = title
                            viewModel.deadlineDate = deadlineDate
                            viewModel.deadlineTime = deadlineTime
                            viewModel.onSave()
                            title = ""
                            showBottomSheet.value = false
                        }) {
                        Text(text = "Save")
                    }
                }
            }
        }

        if (showDateDialog) {
            DatePickerDialog(onDismissRequest = { showDateDialog = false }, confirmButton = {

                TextButton(onClick = {
                    deadlineDate = datePickerState.selectedDateMillis?.let {
                        Instant.ofEpochMilli(it).atZone(
                            ZoneId.systemDefault()
                        ).toLocalDate()
                    }
                    showDateDialog = false
                }) {
                    Text(text = "Save")
                }

            }) {
                DatePicker(/*headline = {
                    DatePickerDefaults.DatePickerHeadline(
                        state = datePickerState,
                        remember { DatePickerFormatter() },
                        modifier = Modifier.padding(18.dp)
                    )
                },*/ headline = null, showModeToggle = false, title = null, state = datePickerState)
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraSmall,
                    onClick = { showTimeDialog = true }) {
                    Text(text = "Set time")
                }
            }
        }

        if (showTimeDialog) {
            AlertDialog(onDismissRequest = { showTimeDialog = false }, confirmButton = {
                TextButton(onClick = {
                    deadlineTime = LocalTime.of(timePickerState.hour, timePickerState.minute)
                    showTimeDialog = false
                }) {
                    Text(text = "Save")
                }
            }, dismissButton = {
                TextButton(onClick = {
                    showTimeDialog = false
                }) {
                    Text(text = "Cancel")
                }
            }, text = {
                TimePicker(state = timePickerState)
            })
        }
    }
}