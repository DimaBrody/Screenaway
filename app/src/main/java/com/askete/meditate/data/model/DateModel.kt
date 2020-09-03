package com.askete.meditate.data.model

data class DateModel(
    val hours: Int,
    val minutes: Int
) {
    companion object {
        fun createDateFromString(string: String): DateModel {
            val hours = string.substringBefore("#").toInt()
            val minutes = string.substringAfter("#").toInt()

            return DateModel(hours,minutes)
        }

        fun createStringFromDate(date: DateModel): String =
            "${date.hours}#${date.minutes}"

        fun createDefault() = DateModel(12,0)

        fun createStringFromInt(hours: Int, minutes: Int) = "$hours#$minutes"
    }

}

