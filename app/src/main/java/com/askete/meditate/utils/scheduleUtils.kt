package com.askete.meditate.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.askete.meditate.data.model.DateModel
import com.askete.meditate.tools.receivers.NotificationReceiver
import java.util.*
import java.util.concurrent.TimeUnit

fun scheduleNotificationNextDay(context: Context) {
    val intent = Intent(context, NotificationReceiver::class.java)
    intent.putExtra("request", true)
    val pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1),
        pendingIntent
    )
}

fun scheduleNotificationByTime(context: Context, timeData: DateModel) {
    val intent = Intent(context, NotificationReceiver::class.java)

    val calendar = Calendar.getInstance()

    calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DATE),timeData.hours, timeData.minutes,0)

    if(System.currentTimeMillis() > calendar.timeInMillis) return

    intent.putExtra("request", true)
    val pendingIntent = PendingIntent.getBroadcast(context, 2, intent, 0)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.setExact(
        AlarmManager.RTC_WAKEUP,
        calendar.timeInMillis,
        pendingIntent
    )
}

fun rescheduleNotification(context: Context, timeData: DateModel) {
    stopSchedule(context)
    scheduleNotificationByTime(context, timeData)
}

fun stopSchedule(context: Context) {
    val pendingIntent =
        PendingIntent.getBroadcast(context, 0, Intent(context, NotificationReceiver::class.java), 0)
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}