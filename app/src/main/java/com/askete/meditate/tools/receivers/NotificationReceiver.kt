package com.askete.meditate.tools.receivers

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.askete.meditate.App.Companion.CHANNEL_ID
import com.askete.meditate.R
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.data.model.DateModel
import com.askete.meditate.ui.MainActivity
import com.askete.meditate.utils.scheduleNotificationByTime
import com.askete.meditate.utils.scheduleNotificationNextDay
import com.askete.meditate.utils.stopSchedule

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        if (prefs.isReminderEnabled)
            if (intent != null && ("android.intent.action.BOOT_COMPLETED" == intent.action || "android.intent.action.QUICKBOOT_POWERON" == intent.action)) {
                scheduleNotificationByTime(
                    context,
                    DateModel.createDateFromString(prefs.savedReminderTime!!)
                )
            } else {
                scheduleNotificationNextDay(context)
                notify(context)
            }
        else stopSchedule(context)
    }

    fun notify(context: Context) {
        val appIntent = Intent(context, MainActivity::class.java)

        val pVolume =
            PendingIntent.getActivity(context, 1, appIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setAutoCancel(true)
            .setContentIntent(pVolume)
            .setWhen(System.currentTimeMillis())
            .setContentTitle("Attention!")
            .setContentText("It's time to take your phone off!")
            .setSmallIcon(R.mipmap.ic_launcher)

        val notification = mBuilder.build()
        notification.flags = notification.flags or Notification.FLAG_AUTO_CANCEL
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notify(
            NOTIFICATION_ID,
            notification
        )
    }

    companion object {
        private const val NOTIFICATION_ID = 123

        var instanceSingleton: NotificationReceiver? = null

        val instance: NotificationReceiver
            get() = instanceSingleton ?: NotificationReceiver().also { instanceSingleton = it }
    }

}