package com.askete.meditate.utils.functions

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.dependencies.requireContextFromDependencies
import java.lang.NullPointerException


fun setupAudioSilent(context: Context) = simpleTry {
    AudioHolder.setupIfAbsent(context)
    AudioHolder.setupAudioSilent()
}

fun setupAudioDefault(context: Context) = simpleTry {
    AudioHolder.setupIfAbsent(context)
    AudioHolder.setupAudioDefault()
}

val isNotificationAccessGranted: Boolean
    get() = AudioHolder.isNotificationPolicyGranted || !prefs.isNotDisturbEnabled

private object AudioHolder {
    private var context: Context? = requireContextFromDependencies()

    private var audioManagerSingleton: AudioManager? = null

    private var notificationManagerSingleton: NotificationManager? = null

    private val audioManager: AudioManager
        get() = audioManagerSingleton
            ?: context?.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
            ?: throw NullPointerException("Context is null!")

    private val notificationManager: NotificationManager
        get() = notificationManagerSingleton
            ?: context?.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager
            ?: throw NullPointerException("Context is null")

    fun setupIfAbsent(context: Context) {
        this.context = context
    }

    fun setupAudioSilent() {
        if(prefs.isNotDisturbEnabled){
            prefs.audioInfo = audioManager.ringerMode
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                prefs.previousNotificationSetting = notificationManager.currentInterruptionFilter;
                notificationManager.setInterruptionFilter(NotificationManager.INTERRUPTION_FILTER_NONE);
            }
        }
    }

    @SuppressLint("WrongConstant")
    fun setupAudioDefault() {
        if(prefs.isNotDisturbEnabled) {
            audioManager.ringerMode = prefs.audioInfo
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                notificationManager.setInterruptionFilter(prefs.previousNotificationSetting);
            }
        }
    }

    val isNotificationPolicyGranted: Boolean
        get() = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                notificationManager.isNotificationPolicyAccessGranted else true

}

const val RC_DISTURB = 321

fun Fragment.openNotDisturb(){
    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
        val intent = Intent(
            Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
        )

        startActivityForResult(intent, RC_DISTURB)
    }
}