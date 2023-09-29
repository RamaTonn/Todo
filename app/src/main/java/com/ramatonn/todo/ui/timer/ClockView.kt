package com.ramatonn.todo.ui.timer

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.hitanshudhawan.circularprogressbar.CircularProgressBar
import com.ramatonn.todo.service.ClockState
import com.ramatonn.todo.service.StopwatchService
import com.ramatonn.todo.service.StopwatchServiceHelper
import com.ramatonn.todo.service.TimerService
import com.ramatonn.todo.service.TimerServiceHelper
import com.ramatonn.todo.util.ACTION_SERVICE_CANCEL
import com.ramatonn.todo.util.ACTION_SERVICE_PAUSE
import com.ramatonn.todo.util.ACTION_SERVICE_RESUME
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopNavigationClockView(
    items: List<String>,
    composables: List<@Composable () -> Unit>,
    pagerState: PagerState,
    clockType: Int
) {
    TopSwipeableNavigationBar(items, composables, pagerState, clockType)
}

@Composable
fun TimerView(service: TimerService) {
    val context = LocalContext.current
    val active by service.activeTimeRemainingMillis
    val rest by service.restTimeRemainingMillis
    val rounds by service.roundsRemaining
    val isRest by service.isRest
    val state by service.clockState


    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (selection, indicator, buttons) = createRefs()
        if (state == ClockState.STOPPED) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .constrainAs(selection) {
                        top.linkTo(parent.top)
                        centerHorizontallyTo(parent)
                    }
            ) {
                Card(modifier = Modifier.padding(vertical = 8.dp)) {
                    RoundSelector(quantity = service.rounds)
                }
                Card(modifier = Modifier.padding(vertical = 8.dp)) {
                    TimeSelector(quantity = service.activeTime)
                    Divider()
                    TimeSelector(quantity = service.restTime)
                }
            }
        } else {
            var progress by remember { mutableStateOf(1f) }
            val animatedProgress by animateFloatAsState(
                targetValue = progress, label = ""
            )
            LaunchedEffect(key1 = rest, key2 = active) {
                progress =
                    (if (isRest) rest.toFloat() / service.restTime.value else active.toFloat() / service.activeTime.value).toFloat()
            }
            ConstraintLayout(modifier = Modifier
                .constrainAs(indicator) {
                    centerTo(parent)
                    width = Dimension.fillToConstraints
                    height = Dimension.fillToConstraints
                }
                .fillMaxSize()) {
                val (indic, text) = createRefs()
                Column(modifier = Modifier.constrainAs(text) {
                    centerTo(parent)
                }, horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${formatMillis(if (isRest) rest else active).first}:${
                            formatMillis(
                                if (isRest) rest else active
                            ).second
                        }",
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "$rounds/${service.rounds.value}",
                        style = MaterialTheme.typography.titleLarge
                    )
                }
                CircularProgressBar(
                    progress = animatedProgress,
                    modifier = Modifier
                        .fillMaxSize(0.75f)
                        .constrainAs(indic) {
                            centerTo(parent)
                        },
                    progressBarColor = if (isRest) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                    backgroundProgressBarColor = MaterialTheme.colorScheme.surfaceVariant,
                    progressMax = 1f,
                    roundBorder = true,
                )
            }
        }
        Row(modifier = Modifier
            .constrainAs(buttons) {
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
            }) {
            Button(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                shape = MaterialTheme.shapes.small,
                onClick = {
                    TimerServiceHelper.triggerForeGroundService(
                        context,
                        if (state == ClockState.STARTED) ACTION_SERVICE_PAUSE else ACTION_SERVICE_RESUME,
                        service.activeTime.value,
                        service.restTime.value,
                        service.rounds.value
                    )
                }) {
                Text(text = if (state == ClockState.STARTED) "Pause" else if (state == ClockState.STOPPED) "Start" else "Resume")
            }
            if (state != ClockState.STOPPED) {
                /*                Divider(
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .width(1.dp)
                                )*/
                Button(modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                    shape = MaterialTheme.shapes.small,
                    onClick = {
                        TimerServiceHelper.triggerForeGroundService(
                            context,
                            ACTION_SERVICE_CANCEL
                        )
                    }) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()

@Composable
fun TimeSelector(quantity: MutableState<Long>) {

    var value by remember { quantity }
    var minutes by remember { mutableStateOf(TextFieldValue(formatMillis(value).first)) }
    var seconds by remember { mutableStateOf(TextFieldValue(formatMillis(value).second)) }

    var kMinutes by remember { mutableStateOf(false) }
    var kSeconds by remember { mutableStateOf(false) }

    if (kMinutes) {
        SideEffect {
            kMinutes = false
        }
    }
    if (kSeconds) {
        SideEffect {
            kSeconds = false
        }
    }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val focusManager = LocalFocusManager.current
        val (increase, decrease, text) = createRefs()
        RepeatingIconButton(
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(increase) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    centerVerticallyTo(parent)
                },
            onClick = {
                if (value < 5999000L){
                    value += 1000
                    minutes = TextFieldValue(formatMillis(value).first)
                    seconds = TextFieldValue(formatMillis(value).second)
                }
            }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp,
                contentDescription = "Increase time"
            )
        }

        ConstraintLayout(modifier = Modifier.constrainAs(text) {
            centerTo(parent)
            absoluteLeft.linkTo(increase.absoluteRight)
            absoluteRight.linkTo(decrease.absoluteLeft)
            width = Dimension.fillToConstraints
        }) {
            val (left, right, center) = createRefs()
            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .onFocusChanged {
                    if (!it.isFocused) {
                        if (minutes.text.isNotBlank()) {
                            value = minutes.text.toLong() * 60 * 1000 + seconds.text.toLong() * 1000
                        }
                        minutes = TextFieldValue(formatMillis(value).first)
                    }
                    if (it.isFocused) {
                        minutes = minutes.copy(
                            selection = TextRange(0, minutes.text.length)
                        )
                        kMinutes = true
                    }
                }
                .constrainAs(left) {
                    absoluteRight.linkTo(center.absoluteLeft)
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    centerVerticallyTo(parent)
                    width = Dimension.preferredWrapContent
                },
                value = minutes,
                onValueChange = {
                    if (kMinutes) {
                        kMinutes = false
                        minutes = if (it.text.isDigitsOnly() || it.text.isBlank()) it.copy(
                            selection = TextRange(0, it.text.length)
                        ) else minutes
                    } else {
                        minutes = if (it.text.isDigitsOnly() || it.text.isBlank()) it else minutes
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus(true)
                }),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )

            Text(
                modifier = Modifier.constrainAs(center) { centerTo(parent) },
                text = ":",
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold)
            )

            BasicTextField(modifier = Modifier
                .width(IntrinsicSize.Min)
                .onFocusChanged {
                    if (!it.isFocused) {
                        if (seconds.text.isNotBlank()) {
                            value = minutes.text.toLong() * 60 * 1000 + seconds.text.toLong() * 1000
                        }
                        seconds = TextFieldValue(formatMillis(value).second)
                    }
                    if (it.isFocused) {
                        seconds = seconds.copy(
                            selection = TextRange(0, seconds.text.length)
                        )
                        kSeconds = true
                    }
                }
                .constrainAs(right) {
                    absoluteLeft.linkTo(center.absoluteRight)
                    absoluteRight.linkTo(parent.absoluteRight)
                    centerVerticallyTo(parent)
                    width = Dimension.preferredWrapContent
                },
                value = seconds,
                onValueChange = {
                    if (kSeconds) {
                        seconds = if (it.text.isDigitsOnly() || it.text.isBlank()) it.copy(
                            selection = TextRange(0, it.text.length)
                        ) else seconds
                    } else {
                        seconds = if (it.text.isDigitsOnly() || it.text.isBlank()) it else seconds
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Number
                ),
                keyboardActions = KeyboardActions(onDone = {
                    focusManager.clearFocus(true)
                }),
                textStyle = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                singleLine = true
            )
        }

        RepeatingIconButton(
            modifier = Modifier.constrainAs(decrease) {
                absoluteRight.linkTo(parent.absoluteRight)
                centerVerticallyTo(parent)
            },
            onClick = {
                if (value > 0){
                    value -= 1000
                    minutes = TextFieldValue(formatMillis(value).first)
                    seconds = TextFieldValue(formatMillis(value).second)
                }
            }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Decrease time"
            )
        }
    }
}

@Composable
fun RoundSelector(quantity: MutableState<Int>) {

    var value by remember { quantity }
    var rounds by remember {
        mutableStateOf(TextFieldValue("${quantity.value}"))
    }

    var kRounds by remember { mutableStateOf(false) }

    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val focusManager = LocalFocusManager.current
        val (increase, decrease, text) = createRefs()

        RepeatingIconButton(
            modifier = Modifier
                .padding(8.dp)
                .constrainAs(increase) {
                    absoluteLeft.linkTo(parent.absoluteLeft)
                    centerVerticallyTo(parent)
                },
            onClick = {
                if (value < 99){
                    value += 1
                    rounds = TextFieldValue(value.toString())
                }
            }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowUp,
                contentDescription = "Increase time"
            )
        }

        BasicTextField(modifier = Modifier
            .width(IntrinsicSize.Min)
            .onFocusChanged {
                if (!it.isFocused) {
                    if (rounds.text.isNotEmpty()) {
                        value = rounds.text.toInt()
                    }
                    rounds = TextFieldValue(value.toString())
                }
                if (it.isFocused) {
                    rounds = rounds.copy(
                        selection = TextRange(0, rounds.text.length)
                    )
                    kRounds = true
                }
            }
            .constrainAs(text) {
                centerTo(parent)
            },
            value = rounds,
            onValueChange = {
                if (kRounds){
                    kRounds = false
                    rounds = if (it.text.isDigitsOnly() || it.text.isBlank()) it.copy(
                        selection = TextRange(0, it.text.length)
                    ) else rounds
                }
                else {
                    rounds = if (it.text.isDigitsOnly() || it.text.isBlank()) it else rounds
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Number
            ),
            keyboardActions = KeyboardActions(onDone = {
                focusManager.clearFocus(true)
            }),
            textStyle = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            singleLine = true
        )

        RepeatingIconButton(
            modifier = Modifier.constrainAs(decrease) {
                absoluteRight.linkTo(parent.absoluteRight)
                centerVerticallyTo(parent)
            },
            onClick = {
                if (value > 0){
                    value -= 1
                    rounds = TextFieldValue(value.toString())
                }
            }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Decrease time"
            )
        }
    }
}

@Composable
fun RepeatingIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit
) {

    var job by remember { mutableStateOf<Job?>(null) }
    var pressed by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .indication(
                interactionSource = interactionSource,
                indication = rememberRipple(
                    bounded = false,
                    radius = 40.dp / 2
                )
            )
            .size(48.dp)
            .clip(CircleShape)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = { offset ->
                        pressed = true

                        val press = PressInteraction.Press(offset)
                        interactionSource.emit(press)

                        job = scope.launch {
                            var delayTime = 500L
                            while (this.isActive) {
                                onClick()
                                delay(delayTime) // delay between each repeat action
                                delayTime = (delayTime * 0.8)
                                    .toLong()
                                    .coerceAtLeast(10)
                            }
                        }
                        tryAwaitRelease()
                        job?.cancel()
                        job = null

                        interactionSource.emit(PressInteraction.Release(press))

                        pressed = false
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}


fun formatMillis(millis: Long): Pair<String, String> {
    val totalSeconds = millis / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return Pair(first = String.format("%02d", minutes), second = String.format("%02d", seconds))
}

@Composable
fun StopwatchView(service: StopwatchService) {
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
            text = service.millisToFormattedString(millis),
            style = MaterialTheme.typography.displayLarge
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
                    Text(text = service.millisToFormattedString(it))
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
                        StopwatchServiceHelper.triggerForeGroundService(
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
                                StopwatchServiceHelper.triggerForeGroundService(
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TopSwipeableNavigationBar(
    items: List<String>,
    composables: List<@Composable () -> Unit>,
    pagerState: PagerState,
    clockType: Int
) {
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        pagerState.scrollToPage(clockType)
    }
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

