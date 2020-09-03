package com.askete.meditate.utils

import android.content.Context
import com.askete.meditate.R
import com.askete.meditate.data.model.DataSnapshot
import java.util.*

fun createTimelineArray(
    context: Context,
    collection: Collection<DataSnapshot>,
    processItem: (DataSnapshot, Calendar?, Calendar?) -> Unit
): List<DataSnapshot> {
    val sortedCollection = collection.sortedByDescending { it.date }

    val newCollection = mutableListOf<DataSnapshot>()

    val iterator = sortedCollection.iterator()

    var prevCalendarDate: Calendar?
    var currentCalendarDate: Calendar? = null

    while (iterator.hasNext()) {
        val item = iterator.next()

        prevCalendarDate = currentCalendarDate
        currentCalendarDate = instantiateCalendar(item.date)

        if (!isTheSameMonthCalendars(prevCalendarDate, currentCalendarDate)) {
            newCollection.add(
                DataSnapshot.createDescOnlyData(
                    createDescWithCalendar(
                        context,
                        currentCalendarDate
                    )
                )
            )
        }

        processItem(item, prevCalendarDate, currentCalendarDate)

        newCollection.add(item)
    }

    return newCollection
}

fun instantiateCalendar(time: Long): Calendar = Calendar.getInstance().also {
    it.timeInMillis = time
}

private fun isTheSameMonthCalendars(calendarOne: Calendar?, calendarTwo: Calendar?): Boolean {
    return if (calendarOne != null && calendarTwo != null) {
        val calendarOneYear = calendarOne.get(Calendar.YEAR)
        val calendarOneMonth = calendarOne.get(Calendar.MONTH)

        val calendarTwoYear = calendarTwo.get(Calendar.YEAR)
        val calendarTwoMonth = calendarTwo.get(Calendar.MONTH)

        calendarOneYear == calendarTwoYear && calendarOneMonth == calendarTwoMonth
    } else false
}


private fun createDescWithCalendar(context: Context, calendar: Calendar): String {
    val monthsArray = context.resources.getStringArray(R.array.months)
    val monthName = monthsArray[calendar.get(Calendar.MONTH)]
    val year = calendar.get(Calendar.YEAR)

    return "$monthName $year"
}
