package ru.versoit.todoapp.data.storage.room

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(timeStamp: Long?) = timeStamp?.let { Date(it) }
}