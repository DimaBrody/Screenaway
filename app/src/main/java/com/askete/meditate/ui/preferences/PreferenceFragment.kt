package com.askete.meditate.ui.preferences

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.data.model.DateModel
import com.askete.meditate.ui.base.BaseSimpleFragment
import com.askete.meditate.ui.dialog.DialogPicker
import com.askete.meditate.utils.*
import com.askete.meditate.utils.animations.fadeFromTo
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_preferences.view.*

class PreferenceFragment : BaseSimpleFragment() {

    private val dailyNotificationEnabledData = MutableLiveData(prefs.isReminderEnabled)

    private var dialogPicker: DialogPicker? = null

    override fun View.createViews() {

        setLightStatusBar(requireActivity())

//        YandexMetrica.reportEvent("PreferenceFragment","onCreate()")

        preferences_back.setOnClickListener {
            clearLightStatusBar(requireActivity())
            navigateMain()
        }

        preferences_daily_reminder_switch.apply {
            isChecked = prefs.isReminderEnabled
            setOnCheckedChangeListener { _, isChecked ->
                prefs.isReminderEnabled = isChecked
                dailyNotificationEnabledData.value = isChecked

                if(isChecked)
                    scheduleNotificationByTime(context,DateModel.createDateFromString(prefs.savedReminderTime!!))
                else
                    stopSchedule(context)
            }
            this@createViews.preferences_daily_reminder_container.setOnClickListener {
                isChecked = !isChecked
                dailyNotificationEnabledData.value = isChecked

                if(isChecked)
                    scheduleNotificationByTime(context,DateModel.createDateFromString(prefs.savedReminderTime!!))
                else
                    stopSchedule(context)
            }


        }

        preferences_not_disturb_switch.apply {
            setOnCheckedChangeListener { _, isChecked ->
                prefs.isNotDisturbEnabled = isChecked
            }
            this@createViews.preferences_not_disturb_container.setOnClickListener {
                isChecked = !isChecked
            }
            isChecked = prefs.isNotDisturbEnabled
        }

        val date = DateModel.createDateFromString(prefs.savedReminderTime!!)
        preferences_schedule_time.text = getTimeFromMinutesHours(date.hours, date.minutes)

        preferences_schedule_container.setOnClickListener {
            val prefsDate = DateModel.createDateFromString(prefs.savedReminderTime!!)
            dialogPicker =
                DialogPicker(requireContext(), prefsDate.hours, prefsDate.minutes) { hour, minute ->
                    preferences_schedule_time.text = getTimeFromMinutesHours(hour, minute)
                    prefs.savedReminderTime = DateModel.createStringFromInt(hour, minute)

                    rescheduleNotification(context,DateModel.createDateFromString(prefs.savedReminderTime!!))
                }
            dialogPicker?.show()

        }
        preferences_schedule_container.isClickable = false

        dailyNotificationEnabledData.observe(this@PreferenceFragment, Observer {
            preferences_schedule_container.isClickable = it
            preferences_schedule_container.fadeFromTo(if (it) .5f else 1f, if (it) 1f else .5f)
        })
    }

    override fun getFragmentType(): Int = FragmentType.SETTINGS
}
