package com.ramatonn.todo.ui.timer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ramatonn.todo.service.ClockService
import com.ramatonn.todo.service.ClockState
import com.ramatonn.todo.service.ServiceHelper
import com.ramatonn.todo.util.ACTION_SERVICE_CANCEL
import com.ramatonn.todo.util.ACTION_SERVICE_PAUSE
import com.ramatonn.todo.util.ACTION_SERVICE_RESUME

@Composable
fun ClockView(service: ClockService) {
    val context = LocalContext.current
    val millis by service.timePassedMillis
    val type by service.clockType
    val state by service.clockState

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(text = service.millisToFormattedString(millis, type))
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
            Column{
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
                        TextButton(
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(0.dp),
                            onClick = {
                                ServiceHelper.triggerForeGroundService(
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
    }
}

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