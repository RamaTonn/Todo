package com.ramatonn.todo.ui.task_list

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramatonn.todo.R
import com.ramatonn.todo.data.task.Task
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.absoluteValue

/*
@Composable
fun TaskView(task: Task, viewModel: TaskListViewModel, onclick: () -> Unit) {

    var fav by remember {
        mutableStateOf(false)
    }

    ListItem(headlineContent = {
        Column {
            Text(
                text = task.title, maxLines = 1, overflow = TextOverflow.Ellipsis
            )

            if (task.deadlineDate != null) {
                Card(modifier = Modifier.padding(2.dp), shape = MaterialTheme.shapes.small) {
                    val (formattedString, isFuture) = task.deadlineDate.formatRelativeDate(
                        LocalDate.now()
                    )
                    Text(
                        text = formattedString,
                        modifier = Modifier.padding(2.dp),
                        color = if (isFuture) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }, leadingContent = {
        Checkbox(modifier = Modifier.padding(horizontal = 16.dp),
            checked = task.complete,
            onCheckedChange = {
                viewModel.onEvent(TaskListEvent.OnCompletedChange(task, it))

            })
    }, trailingContent = {
        IconToggleButton(checked = fav, onCheckedChange = { fav = !fav }) {
            if (!fav) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.round_star_border_24),
                    contentDescription = "Favourite"
                )
            } else {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.round_star_24),
                    contentDescription = "Favourite",
                    tint = MaterialTheme.colorScheme.secondary
                )
            }

        }
    }, modifier = Modifier.clickable { onclick() })
    Divider()
}
*/

@Composable
fun TaskItem(task: Task, onEvent: (TaskListEvent) -> Unit, modifier: Modifier = Modifier) {

    val tint by animateColorAsState(
        targetValue = if (task.favourite) MaterialTheme.colorScheme.secondary else Color.Unspecified,
        label = ""
    )

    ListItem(modifier = modifier.clickable {
        onEvent(TaskListEvent.OnAddEditTaskClick(task))
    }, shadowElevation = 6.dp, headlineContent = {

        Text(
            modifier = Modifier.padding(16.dp), text = task.title
        )

    }, leadingContent = {

        Checkbox(modifier = Modifier.size(24.dp).padding(horizontal = 16.dp), checked = task.complete, onCheckedChange = {
            onEvent(TaskListEvent.OnCompletedChange(task, it))
        })

    }, trailingContent = {

        IconToggleButton(
            modifier = Modifier.size(24.dp),
            checked = task.favourite,
            onCheckedChange = {
                onEvent(TaskListEvent.OnFavouriteChange(task, it))
            }) {
            Icon(
                modifier = Modifier.size(24.dp),
                imageVector = ImageVector.vectorResource(id = if (task.favourite) R.drawable.round_star_24 else R.drawable.round_star_border_24),
                contentDescription = "Favourite",
                tint = tint
            )
        }

    })
}

@Composable
fun animateTextStyleAsState(
    targetValue: TextStyle,
    animationSpec: AnimationSpec<Float> = spring(),
    finishedListener: ((TextStyle) -> Unit)? = null
): State<TextStyle> {

    val animation = remember { Animatable(0f) }
    var previousTextStyle by remember { mutableStateOf(targetValue) }
    var nextTextStyle by remember { mutableStateOf(targetValue) }

    val textStyleState = remember(animation.value) {
        derivedStateOf {
            lerp(previousTextStyle, nextTextStyle, animation.value)
        }
    }

    LaunchedEffect(targetValue, animationSpec) {
        previousTextStyle = textStyleState.value
        nextTextStyle = targetValue
        animation.snapTo(0f)
        animation.animateTo(1f, animationSpec)
        finishedListener?.invoke(textStyleState.value)
    }

    return textStyleState
}

fun LocalDate.formatRelativeDate(date: LocalDate): Pair<String, Boolean> {

    val period = date.until(this)

    val years = period.years.absoluteValue

    val months = period.months.absoluteValue

    val days = period.days.absoluteValue

    var string = ""

    val isFuture = !(period.days < 0 || period.months < 0 || period.years < 0)

    if (period.isZero) {
        return Pair("Today", isFuture)
    } else if (isFuture) {
        string = this.format(DateTimeFormatter.ofPattern("d, MMM"))
    } else {
        if (years > 0) {
            string = when (years) {
                1 -> "1 year ago"
                else -> "$years years ago"
            }
        } else if (months > 0) {
            string = when (months) {
                1 -> "1 month ago"
                else -> "$months months ago"
            }
        } else if (days > 7) {
            string = when (days % 7) {
                1 -> "1 week ago"
                else -> "${days % 7} weeks ago"
            }
        } else {
            string = when (days) {
                1 -> "Yesterday"
                else -> "$days days ago"
            }
        }
    }

    return Pair(string, isFuture)
}

@Preview
@Composable
fun TaskItemPreview() {
    val task = Task(title = "Sample task")
    var isComp by remember {
        mutableStateOf(false)
    }
    var isFav by remember {
        mutableStateOf(false)
    }
    TaskItem(task = task, onEvent = {
        when (it) {
            is TaskListEvent.OnCompletedChange -> isComp = !isComp
            is TaskListEvent.OnFavouriteChange -> isFav = !isFav
            else -> {}
        }
    })
}