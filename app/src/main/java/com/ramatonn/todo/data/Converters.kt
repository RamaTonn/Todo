package com.ramatonn.todo.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonParser
/*TODO Change GSON to kotlin serializable*/
import com.ramatonn.todo.data.task.RepeatPattern
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun fromLocalDateTime(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }

    @TypeConverter
    fun toLocalDate(date: String): LocalDate {
        return LocalDate.parse(date)
    }

    @TypeConverter
    fun toLocalTime(time: String): LocalTime {
        return LocalTime.parse(time)
    }

    @TypeConverter
    fun toLocalDateTime(dateTime: String): LocalDateTime {
        return LocalDateTime.parse(dateTime)
    }

    @TypeConverter
    fun fromRepeatPattern(repeatPattern: RepeatPattern): String{
        return Gson().toJson(repeatPattern)
    }

    @TypeConverter
    fun toRepeatPattern(json: String): RepeatPattern {
        var output: RepeatPattern = RepeatPattern.Daily
        val obj = JsonParser.parseString(json).asJsonObject
        when(obj["tag"].asString){
            "DAILY" -> output = RepeatPattern.Daily
            "WEEKLY" -> output = Gson().fromJson(json, RepeatPattern.Weekly::class.java)
            "MONTHLY_DAY" -> output = Gson().fromJson(json, RepeatPattern.MonthlyOnDay::class.java)
            "MONTHLY_WEEK" -> output = Gson().fromJson(json, RepeatPattern.MonthlyOnSpecificWeek::class.java)
            "YEARLY" -> output = Gson().fromJson(json, RepeatPattern.Yearly::class.java)
        }
        return output
    }

    @TypeConverter
    fun fromList(list: List<Int>): String{
        return list.joinToString(",")
    }

    @TypeConverter
    fun toList(list: String): List<Int>{
        return list.split(",").map { it.toInt() }
    }
}