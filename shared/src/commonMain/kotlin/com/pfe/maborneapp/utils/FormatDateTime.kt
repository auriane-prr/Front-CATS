package com.pfe.maborneapp.utils

import kotlinx.datetime.*

fun formatDateOnly(dateTime: String): String {
    return try {
        val instant = Instant.parse(dateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
        "${localDateTime.dayOfMonth.toString().padStart(2, '0')}/${localDateTime.monthNumber.toString().padStart(2, '0')}/${localDateTime.year}"
    } catch (e: Exception) {
        println("DEBUG: Erreur dans formatDateOnly : ${e.message}")
        "Format incorrect"
    }
}

fun formatTimeOnly(dateTime: String): String {
    return try {
        val instant = Instant.parse(dateTime)
        val localDateTime = instant.toLocalDateTime(TimeZone.UTC)
        "${localDateTime.hour.toString().padStart(2, '0')}:${localDateTime.minute.toString().padStart(2, '0')}"
    } catch (e: Exception) {
        println("DEBUG: Erreur dans formatTimeOnly : ${e.message}")
        "Format incorrect"
    }
}
