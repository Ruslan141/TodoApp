package ru.versoit.data.storage.room

import androidx.room.TypeConverter
import java.util.Date

/**
 * Converts date to a convenient representation format in room database.
 */
class DateConverter {

    @TypeConverter
    fun fromDate(date: Date?) = date?.time

    @TypeConverter
    fun toDate(timeStamp: Long?) = timeStamp?.let { Date(it) }
}