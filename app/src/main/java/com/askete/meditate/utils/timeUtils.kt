package com.askete.meditate.utils

import java.util.*

fun getTimeFromTicks(tick: Long): String {
    val seconds = tick % 60
    val minutes = tick / 60

    val secondText = "${if (seconds < 10) "0$seconds" else seconds}"
    val minuteText = "${if (minutes < 10) "0$minutes" else minutes}"

    return "$minuteText:$secondText"
}

fun getTimeFromMinutesHours(hours: Int, minutes: Int): String {
    val isSecondHalfOfDay = hours !in 0..11
    val correctHours = when (hours) {
        0 -> 12
        in 1..12 -> hours
        in 13..23 -> hours - 12
        else -> hours
    }
    val correctMinutes = if (minutes < 10) "0$minutes" else minutes

    return "$correctHours:$correctMinutes ${if (isSecondHalfOfDay) "PM" else "AM"}"
}

val randomCalendarDate: Long
    get() {
        val calendar = Calendar.getInstance()
        calendar.set(
            2019,
            randomInt(11, 11),
            randomInt(25, 28),
            randomInt(0, 23),
            randomInt(0, 59),
            randomInt(0, 59)
        )
        return calendar.timeInMillis
    }

fun randomInt(min: Int, max: Int): Int {
    return min + (Math.random() * ((max - min) + 1)).toInt()
}

fun getNormalTimeFromCalendar(calendar: Calendar) : String {
    val hours = calendar.get(Calendar.HOUR)
    val minutes = calendar.get(Calendar.MINUTE)

    val minuteText = "${if (minutes < 10) "0$minutes" else minutes}"

    return "$hours:$minuteText ${getAmOrPmString(calendar.get(Calendar.AM_PM))}"
}

private fun getAmOrPmString(id: Int) = if(id == Calendar.AM) "AM" else "PM"
