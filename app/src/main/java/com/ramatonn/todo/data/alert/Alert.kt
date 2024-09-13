package com.ramatonn.todo.data.alert

import android.content.ContentResolver
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalTime

@Entity
data class Alert(
    val name: String,
    val message: String,
    val period: Long,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val soundAddress: String? = "${ContentResolver.SCHEME_ANDROID_RESOURCE}://com.ramatonn.todo/raw/beep",
    var enabled: Boolean = true,
    var isMute: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)
