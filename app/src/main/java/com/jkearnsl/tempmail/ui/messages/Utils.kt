package com.jkearnsl.tempmail.ui.messages

import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


fun generateColorFromEmail(email: String): Int {
    val hash = email.hashCode()
    val r = (hash and 0xFF0000) shr 16
    val g = (hash and 0x00FF00) shr 8
    val b = (hash and 0x0000FF)
    return Color.rgb(r, g, b)
}

fun formatDate(date: Date): String {
    val now = Calendar.getInstance().time
    val diff = now.time - date.time

    return when {
        TimeUnit.MILLISECONDS.toDays(diff) < 1 -> {
            val hours = date.hours.toString().padStart(2, '0')
            val minutes = date.minutes.toString().padStart(2, '0')

            when {
                TimeUnit.MILLISECONDS.toMinutes(diff) < 1 -> {
                    if (Locale.getDefault().language == "ru") {
                        "Только что"
                    } else {
                        "Just now"
                    }
                }
                else -> {
                    "$hours:$minutes"
                }
            }
        }
        date.month == now.month && date.year == now.year -> {
            val outputFormat = SimpleDateFormat("dd MMMM", Locale.getDefault())
            outputFormat.format(date)
        }
        else -> {
            val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            outputFormat.format(date)
        }
    }
}