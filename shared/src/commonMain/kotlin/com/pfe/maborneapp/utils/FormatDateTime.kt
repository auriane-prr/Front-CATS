package com.pfe.maborneapp.utils

import kotlinx.datetime.*

fun formatDateTime(dateTime: String): String {
    return try {

        val instant = Instant.parse(dateTime)

        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)

        val formattedDate = "${localDateTime.dayOfMonth.toString().padStart(2, '0')}-${localDateTime.monthNumber.toString().padStart(2, '0')}-${localDateTime.year} - ${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"

        formattedDate
    } catch (e: Exception) {
        println("DEBUG: Erreur dans formatDateTime : ${e.message}")
        "Format incorrect"
    }
}

