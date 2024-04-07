package ir.hoseinsa.eyecareapp.utils

import java.util.concurrent.TimeUnit

object TimeUtils {
    fun Long.toTimeFormat(): String {
        val hours = TimeUnit.MILLISECONDS.toHours(this)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(this - TimeUnit.HOURS.toMillis(hours))
        val seconds = TimeUnit.MILLISECONDS.toSeconds(this - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes))
        return String.format("%02d:%02d", minutes, seconds)

    }
}

