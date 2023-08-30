package com.ramatonn.todo.data

import androidx.room.TypeConverter
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
    fun fromRepeatability(repeatability: Repeatability): String{
        return "${repeatability.days},${repeatability.months},${repeatability.years}"
    }

    @TypeConverter
    fun toRepeatability(repeatability: String): Repeatability{
        val list = repeatability.split(",")
        return Repeatability(list[0].toInt(),list[1].toInt(),list[2].toInt())
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