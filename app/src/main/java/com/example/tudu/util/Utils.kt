package com.example.tudu.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Utils {

    fun formatDateFromMillis(millis: Long, pattern: String = "MMM dd, yyyy"): String {
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(Date(millis))
    }

    fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val formattedHour = if (hour % 12 == 0) 12 else hour % 12
        return String.format("%02d:%02d %s", formattedHour, minute, amPm)
    }
}