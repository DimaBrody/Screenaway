package com.askete.meditate.tools.prefs

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.askete.meditate.data.model.DateModel
import com.askete.meditate.utils.delegates.boolean
import com.askete.meditate.utils.delegates.int
import com.askete.meditate.utils.delegates.long
import com.askete.meditate.utils.delegates.string

class PreferenceProvider(
    private val mContext: Context
) {

    private val context: Context
        get() = mContext.applicationContext

    private val prefs: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    var selectedTimeId by prefs.int()
    var audioInfo by prefs.int()

    var previousNotificationSetting by prefs.int()
    var currentBackgroundMain by prefs.int()

    var currentTime: Long by prefs.long()

    var isReminderEnabled by prefs.boolean()
    var isNotDisturbEnabled by prefs.boolean(true)
    var isFirstLaunch by prefs.boolean(true)
    var isEnd by prefs.boolean(true) {
        if (it) currentTime = 0
    }

    var savedReminderTime by prefs.string(
        DateModel.createStringFromDate(DateModel.createDefault())
    )

    fun setDefaultTime() {
        selectedTimeId = 0
    }


}