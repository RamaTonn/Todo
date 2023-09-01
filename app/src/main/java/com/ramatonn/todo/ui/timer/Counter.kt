package com.ramatonn.todo.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.ramatonn.todo.ui.theme.urbanistFont
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration

@Composable
fun Counter(
    isPaused: Boolean,
    interval: Long,
    startingValue: Long,
    content: @Composable (remainingMillis: Long) -> Unit
) {
    var elapsedTime by remember { mutableStateOf(startingValue) }

    LaunchedEffect(isPaused) {
        var startTime = System.nanoTime()
        var accumulatedTime = startingValue

        while (!isPaused) {
            withContext(Dispatchers.Default) {
                delay(interval)
            }

            val currentTime = System.nanoTime()
            val elapsedMillis = (currentTime - startTime) / 1_000_000
            accumulatedTime += elapsedMillis

            elapsedTime = accumulatedTime // Update elapsedTime with the accumulated time
            startTime = currentTime
        }
    }

    content(elapsedTime)
}

@Preview
@Composable
fun TimerPicker(started: MutableState<Boolean> = mutableStateOf(false)) {
    var startingValue by remember {
        mutableStateOf(0L)
    }
    var isPaused by remember {
        mutableStateOf(false)
    }

    var hourPicker by remember {
        mutableStateOf("00")
    }
    var minutePicker by remember {
        mutableStateOf("00")
    }
    var secondPicker by remember {
        mutableStateOf("00")
    }
    var formattedString = "$hourPicker : $minutePicker : $secondPicker"

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.weight(0.05f))
            Text(text = "Hours")
            Spacer(modifier = Modifier.weight(0.2f))
            Text(text = "Minutes")
            Spacer(modifier = Modifier.weight(0.2f))
            Text(text = "Seconds")
            Spacer(modifier = Modifier.weight(0.05f))
        }
        Divider()
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Picker(list = (0..99).map { "%02d".format(it) },
                modifier = Modifier.weight(0.3f),
                onValueChanged = { hourPicker = it })
            Picker(list = (0..59).map { "%02d".format(it) },
                modifier = Modifier.weight(0.3f),
                onValueChanged = { minutePicker = it })
            Picker(list = (0..59).map { "%02d".format(it) }, modifier = Modifier.weight(0.3f),

                onValueChanged = { secondPicker = it })
        }
        Divider()
        Spacer(modifier = Modifier.size(600.dp))
        Button(onClick = { started.value = true }) {

        }
    }
}


fun nanoToString(nanos: Long): String {
    val duration = Duration.ofNanos(nanos)
    val hours = duration.toHours()
    val minutes = duration.toMinutes() % 60
    val seconds = duration.seconds % 60
    val milliseconds = duration.toMillis() % 1000

    return String.format("%02d:%02d:%02d.%02d", hours, minutes, seconds, milliseconds)
}

fun stringToNano(string: String): Long {
    val components = string.split(":")

    val hours = components[0].toInt()
    val minutes = components[1].toInt()
    val seconds = components[2].substringBefore('.').toInt()
    val milliseconds = components[2].substringAfter('.').toInt() * 10

    return (hours * 3600000000000L) + (minutes * 60000000000L) + (seconds * 1000000000L) + (milliseconds * 1000000L)
}

@Composable
fun Picker(
    modifier: Modifier, list: List<String>, onValueChanged: (String) -> Unit, size: Double = 1.0
) {

    val coroutineScope = rememberCoroutineScope()

    var currentValue by remember { mutableStateOf("") }
    val listState = rememberLazyListState(Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % list.size)

    LaunchedEffect(listState.isScrollInProgress) {
        coroutineScope.launch {
            onValueChanged(currentValue)
            listState.animateScrollToItem(index = listState.firstVisibleItemIndex)
        }
    }

    LazyColumn(
        modifier = modifier.height((140 * size).dp),
        state = listState,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(count = Int.MAX_VALUE, itemContent = {
            val index = it % list.size
            if (it == listState.firstVisibleItemIndex + 1) {
                currentValue = list[index]
            }
            Spacer(modifier = Modifier.height((10 * size).dp))
            Text(
                modifier = Modifier.alpha(if (it == listState.firstVisibleItemIndex + 1) 1f else 0.3f),
                text = list[index].uppercase(),
                style = TextStyle(
                    textAlign = TextAlign.Center,
                    fontFamily = urbanistFont,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 0.02.em,
                    fontSize = (22 * size).sp
                )
            )
            Spacer(modifier = Modifier.height((10 * size).dp))
        })
    }
}