package com.guicarneirodev.hoopreel.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String.formatIsoDate(): String {
    return try {
        val dateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
        dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    } catch (e: DateTimeParseException) {
        this
    }
}