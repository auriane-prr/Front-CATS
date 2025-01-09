package com.pfe.maborneapp.utils

import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatDateTime(dateTime: String): String {
    return try {
        // Parse the ISO 8601 datetime string
        val instant = Instant.parse(dateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        // Format the datetime to "dd-MM-yyyy - HH:mm"
        "${localDateTime.dayOfMonth.toString().padStart(2, '0')}-${localDateTime.monthNumber.toString().padStart(2, '0')}-${localDateTime.year} - ${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
    } catch (e: Exception) {
        "Format incorrect"
    }
}

