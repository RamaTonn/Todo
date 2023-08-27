package com.ramatonn.todo.ui.task_list

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.lerp
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.ramatonn.todo.R
import com.ramatonn.todo.data.Task
import kotlinx.coroutines.launch

@Composable
fun TaskItem(task: Task, viewModel: TaskListViewModel) {
    Row(
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center,

        modifier = Modifier
            .padding(12.dp)
            .border(border = ButtonDefaults.outlinedButtonBorder, shape = RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
            .clickable(
                onClick = { /*TODO*/ },
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true)
            )
            .padding(12.dp)
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Checkbox(
            checked = task.complete,
            onCheckedChange = { viewModel.onEvent(TaskListEvent.OnCompletedChange(task = task, it)) },
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.size(20.dp))
        Text(text = task.title)

        Spacer(modifier = Modifier.weight(1.0f))  //<------- THIS SPACER

        OutlinedIconButton(
            onClick = {viewModel.onEvent(TaskListEvent.OnTaskEditClick(task = task))},
            modifier = Modifier.size(36.dp),
            border = ButtonDefaults.outlinedButtonBorder,
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Edit,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF0DCAF0)
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        OutlinedIconButton(
            onClick = {viewModel.onEvent(TaskListEvent.DeleteTaskClick(task = task))},
            modifier = Modifier.size(36.dp),
            border = ButtonDefaults.outlinedButtonBorder,
            shape = RoundedCornerShape(4.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFFD34936),

                )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskView(task: Task, viewModel: TaskListViewModel, onclick: () -> Unit) {

    var fav by remember {
        mutableStateOf(false)
    }

    val strike = MaterialTheme.typography.headlineSmall.copy(textDecoration = TextDecoration.LineThrough)

    val noStrike = MaterialTheme.typography.headlineSmall


    Card(onClick = { onclick() }, modifier = Modifier.padding(6.dp)) {
        Row {
            Checkbox(
                checked = task.complete,
                onCheckedChange = {
                    viewModel.onEvent(TaskListEvent.OnCompletedChange(task, it))

                })
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    modifier = Modifier.fillMaxHeight(),
                    text = task.title,
                    style = if (task.complete) {
                        strike
                    } else {
                        noStrike
                    })

                if (!(task.deadlineDate == null && task.deadlineTime == null)){
                    Card {

                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconToggleButton(checked = fav, onCheckedChange = { fav = !fav }) {
                if(!fav) { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.round_star_border_24), contentDescription = "Favourite") }
                else { Icon(imageVector = ImageVector.vectorResource(id = R.drawable.round_star_24), contentDescription = "Favourite", tint = MaterialTheme.colorScheme.secondary) }

            }
        }
    }
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