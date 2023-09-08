package com.ramatonn.todo.ui.timer

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.ramatonn.todo.service.StopwatchService
import com.ramatonn.todo.service.ClockState
import com.ramatonn.todo.service.ClockType
import com.ramatonn.todo.service.ServiceHelper
import com.ramatonn.todo.util.ACTION_SERVICE_CANCEL
import com.ramatonn.todo.util.ACTION_SERVICE_PAUSE
import com.ramatonn.todo.util.ACTION_SERVICE_RESUME
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopNavigationClockView(
    items: List<String>,
    composables: List<@Composable () -> Unit>,
    pagerState: PagerState,
    service: StopwatchService
) {

    TopSwipeableNavigationBar(items, composables, pagerState)
}

@Composable
fun StopwatchView(service: StopwatchService) {
    service.clockType.value = ClockType.STOPWATCH
    val context = LocalContext.current
    val millis by service.timePassedMillis
    val state by service.clockState
    val laps = remember {
        mutableStateListOf<Long>()
    }
    var lapped by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (stopwatch, lapList, buttons) = createRefs()
        Text(
            modifier = Modifier.constrainAs(stopwatch) { centerTo(parent) },
            text = service.millisToFormattedString(millis, ClockType.STOPWATCH)
        )

        if (lapped) {
            LazyColumn(modifier = Modifier.constrainAs(lapList) {
                centerHorizontallyTo(stopwatch)
                top.linkTo(stopwatch.bottom)
                bottom.linkTo(buttons.top)
                height = Dimension.fillToConstraints
                width = Dimension.matchParent
            }, horizontalAlignment = Alignment.CenterHorizontally) {
                items(laps) {
                    Text(text = service.millisToFormattedString(it, service.clockType.value))
                }
            }
        }

        Column(modifier = Modifier.constrainAs(buttons) { bottom.linkTo(parent.bottom) }) {
            Divider()
            Row(modifier = Modifier.height(40.dp)) {
                TextButton(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(0.dp),
                    onClick = {
                        ServiceHelper.triggerForeGroundService(
                            context,
                            if (state == ClockState.STARTED) ACTION_SERVICE_PAUSE else ACTION_SERVICE_RESUME
                        )
                    }) {
                    Text(text = if (state == ClockState.STARTED) "Pause" else if (state == ClockState.STOPPED) "Start" else "Resume")
                }
                if (state != ClockState.STOPPED) {
                    Divider(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(1.dp)
                    )
                    TextButton(modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(0.dp),
                        onClick = {
                            if (state == ClockState.STARTED) {
                                lapped = true
                                laps.add(millis)
                            } else {
                                laps.clear()
                                ServiceHelper.triggerForeGroundService(
                                    context, ACTION_SERVICE_CANCEL
                                )
                            }
                        }) {
                        Text(text = if (state == ClockState.STARTED) "Lap" else "Cancel")
                    }
                }
            }
        }

    }
}

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerView(service: ClockService) {
    service.clockType.value = ClockType.TIMER
    val context = LocalContext.current
    val millis by service.timePassedMillis
    val state by service.clockState

    var started by remember { mutableStateOf(false) }
    var active by remember { mutableStateOf(true) }
    var minutePicker by remember { mutableStateOf(0) }
    var secondPicker by remember { mutableStateOf(0) }

    var showDialog by remember { mutableStateOf(false) }

    var buttonPressed by remember { mutableStateOf(ButtonPressed.ACTIVE) }

    var activeMinutes by remember { mutableStateOf(3) }
    var activeSeconds by remember { mutableStateOf(0) }

    var activeMinutesIncrease by remember { mutableStateOf(0) }
    var activeSecondsIncrease by remember { mutableStateOf(0) }

    var restMinutes by remember { mutableStateOf(1) }
    var restSeconds by remember { mutableStateOf(0) }

    var restMinutesIncrease by remember { mutableStateOf(0) }
    var restSecondsIncrease by remember { mutableStateOf(0) }

    var rounds by remember { mutableStateOf(3) }

    var remainingMillis by remember { mutableStateOf<Long>(0) }

    var counter by remember { mutableStateOf(0) }

    LaunchedEffect(key1 = started) {
        remainingMillis = when (active) {
            true -> {
                restMinutes * 60 * 1000 + restSeconds * 1000L + counter * (restMinutesIncrease * 60 * 1000 + restSecondsIncrease * 1000)
            }

            false -> {
                activeMinutes * 60 * 1000 + activeSeconds * 1000L + counter * (activeMinutesIncrease * 60 * 1000 + activeSecondsIncrease * 1000)
            }
        }
    }

    if (remainingMillis <= 0) {
        ServiceHelper.triggerForeGroundService(context, ACTION_SERVICE_CANCEL)
        when (active) {
            true -> {
                remainingMillis =
                    restMinutes * 60 * 1000 + restSeconds * 1000L + counter * (restMinutesIncrease * 60 * 1000 + restSecondsIncrease * 1000)
            }

            false -> {
                remainingMillis =
                    activeMinutes * 60 * 1000 + activeSeconds * 1000L + counter * (activeMinutesIncrease * 60 * 1000 + activeSecondsIncrease * 1000)
                counter += 1
            }
        }
        active = !active
        ServiceHelper.triggerForeGroundService(context, ACTION_SERVICE_RESUME)
    }

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (column, buttons) = createRefs()

        if (!started) {
            Column(modifier = Modifier.constrainAs(column) { top.linkTo(parent.top) }) {

                Row {
                    Button(
                        onClick = {
                            buttonPressed = ButtonPressed.ACTIVE
                            showDialog = true
                        },
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(3f)
                    ) {
                        Text(
                            text = "Active = ${"%02d".format(activeMinutes)}:${
                                "%02d".format(
                                    activeSeconds
                                )
                            }"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            buttonPressed = ButtonPressed.ACTIVE_INC
                            showDialog = true
                        },
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(3f)
                    ) {
                        Text(
                            text = "Increase = ${"%02d".format(activeMinutesIncrease)}:${
                                "%02d".format(
                                    activeSecondsIncrease
                                )
                            }"
                        )
                    }
                }
                Row {
                    Button(
                        onClick = {
                            buttonPressed = ButtonPressed.REST
                            showDialog = true
                        },
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(3f)
                    ) {
                        Text(
                            text = "Rest = ${"%02d".format(restMinutes)}:${
                                "%02d".format(
                                    restSeconds
                                )
                            }"
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            buttonPressed = ButtonPressed.REST_INC
                            showDialog = true
                        },
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(3f)
                    ) {
                        Text(
                            text = "Increase = ${"%02d".format(restMinutesIncrease)}:${
                                "%02d".format(
                                    restSecondsIncrease
                                )
                            }"
                        )
                    }
                }
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = {
                            buttonPressed = ButtonPressed.ROUND
                            showDialog = true
                        },
                        shape = MaterialTheme.shapes.extraSmall,
                        modifier = Modifier
                            .padding(12.dp)
                            .weight(3f)
                    ) {
                        Text(text = "Rounds = $rounds")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }

            }
        } else {
            Text(text = service.millisToFormattedString(remainingMillis, service.clockType.value))
        }
        Row(modifier = Modifier.constrainAs(buttons) {
            centerHorizontallyTo(parent)
            bottom.linkTo(parent.bottom)
        }) {
            TextButton(
                onClick = {
                    remainingMillis = activeMinutes * 60 * 1000 + activeSeconds * 1000 - millis
                    ServiceHelper.triggerForeGroundService(
                        context,
                        ACTION_SERVICE_RESUME
                    )
                    started = true
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = "Start")
            }
        }
    }

    if (showDialog) {
        DatePickerDialog(onDismissRequest = { showDialog = false }, confirmButton = { */
/*NULL*//*
 }) {

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                when (buttonPressed) {
                    ButtonPressed.ROUND -> {
                        Picker(
                            list = (0..99).map { it.toString() },
                            onValueChanged = { minutePicker = it.toInt() },
                            firstVisible = rounds,
                            size = 0.8,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    else -> {
                        Picker(
                            list = (0..99).map { it.toString() },
                            onValueChanged = { minutePicker = it.toInt() },
                            firstVisible = when (buttonPressed) {
                                ButtonPressed.ACTIVE -> activeMinutes
                                ButtonPressed.ACTIVE_INC -> activeMinutesIncrease
                                ButtonPressed.REST -> restMinutes
                                ButtonPressed.REST_INC -> restMinutesIncrease
                                else -> 0
                            },
                            size = 0.8,
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp)
                        )
                        Text(
                            text = ":",
                            modifier = Modifier.weight(0.25f),
                            textAlign = TextAlign.Center
                        )
                        Picker(
                            list = (0..59).map { it.toString() },
                            onValueChanged = { secondPicker = it.toInt() },
                            firstVisible = when (buttonPressed) {
                                ButtonPressed.ACTIVE -> activeSeconds
                                ButtonPressed.ACTIVE_INC -> activeSecondsIncrease
                                ButtonPressed.REST -> restSeconds
                                ButtonPressed.REST_INC -> restSecondsIncrease
                                else -> 0
                            },
                            size = 0.8,
                            modifier = Modifier
                                .weight(1f)
                                .padding(6.dp)
                        )
                    }
                }
            }
            Divider()
            TextButton(
                onClick = {
                    when (buttonPressed) {
                        ButtonPressed.ACTIVE -> {
                            activeMinutes = minutePicker
                            activeSeconds = secondPicker
                        }

                        ButtonPressed.ACTIVE_INC -> {
                            activeMinutesIncrease = minutePicker
                            activeSecondsIncrease = secondPicker
                        }

                        ButtonPressed.REST -> {
                            restMinutes = minutePicker
                            restSeconds = secondPicker
                        }

                        ButtonPressed.REST_INC -> {
                            restMinutesIncrease = minutePicker
                            restSecondsIncrease = secondPicker
                        }

                        ButtonPressed.ROUND -> {
                            rounds = minutePicker
                        }
                    }
                    showDialog = false
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text(text = "Confirm")
            }

        }
    }
}
*/

enum class ButtonPressed {
    ACTIVE, ACTIVE_INC, REST, REST_INC, ROUND
}

/*
@Composable
fun EmptyClockView() {
    val context = LocalContext.current
    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text("00:00")
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            Row {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        ServiceHelper.triggerForeGroundService(context, ACTION_SERVICE_RESUME)
                    }) {
                    Text(text = "Start")
                }
            }
        }
    }
}
*/

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSwipeableNavigationBar(
    items: List<String>,
    composables: List<@Composable () -> Unit>,
    pagerState: PagerState,
) {
    val coroutineScope = rememberCoroutineScope()

    Column {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            items.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = { Text(title) },
                )
            }
        }
        HorizontalPager(pageCount = items.size, state = pagerState) {
            for (i in composables.indices) {
                if (pagerState.currentPage == i) {
                    composables[i]()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun TopNavPreview() {
    val items = listOf("Page 1", "Page 2", "Page 3")
    val pagerState = rememberPagerState()
    val composables = listOf<@Composable () -> Unit>({
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "This is Page 1")
        }
    }, {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "This is Page 2")
        }
    }, {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(text = "This is Page 3")
        }
    })
    TopSwipeableNavigationBar(items = items, pagerState = pagerState, composables = composables)
}
