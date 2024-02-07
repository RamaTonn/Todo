package com.ramatonn.todo.ui.alerts

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ramatonn.todo.R
import com.ramatonn.todo.data.Alert
import java.time.LocalTime

@Composable
fun AlertItem(alert: Alert, viewModel: AlertScreenViewModel? = null) {
    val name = alert.name
    var enabled by remember { mutableStateOf(alert.enabled) }
    var isMute by remember { mutableStateOf(alert.isMute) }
    Card(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {

            Text(text = name, )
            Spacer(modifier = Modifier.weight(6f))
            IconButton(onClick = {
                isMute = !isMute
                alert.isMute = isMute
            }) {
                if(isMute) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.round_volume_off_24),
                        contentDescription = null
                    )
                }
                else {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.round_volume_up_24),
                        contentDescription = null
                    )
                }
            }
            Checkbox(checked = enabled, onCheckedChange = {
                enabled = it
                alert.enabled = it
            })
        }
    }
}


@Preview
@Composable
fun AlertItemPreview() {
    val alert = Alert(
        name = "Alert",
        message = "This is a test alert",
        period = 60,
        startTime = LocalTime.now(),
        endTime = LocalTime.now()
    )
    AlertItem(alert = alert)
}