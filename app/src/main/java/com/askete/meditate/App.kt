package com.askete.meditate

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import com.askete.meditate.components.dependencies.initDependencies
import com.askete.meditate.tools.test.TestReceiver
import com.facebook.FacebookSdk
import com.yandex.metrica.YandexMetrica
import com.yandex.metrica.YandexMetricaConfig


class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initDependencies { this }

        TestReceiver.register(this)

        createNotificationChannel()

//        FacebookSdk.sdkInitialize(this)
//        FacebookSdk.setAutoLogAppEventsEnabled(true);

        debugMode = resources.getBoolean(R.bool.debug_mode)

//        val config =
//            YandexMetricaConfig.newConfigBuilder(resources.getString(R.string.yandex_api)).build()
//        YandexMetrica.activate(applicationContext, config)
//        YandexMetrica.enableActivityAutoTracking(this)

        handler = Handler(Looper.getMainLooper())
    }

    companion object {
        var handler: Handler? = null
            private set

        const val CHANNEL_ID = BuildConfig.APPLICATION_ID

        var debugMode: Boolean = false
            private set
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "MainChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager)
                .createNotificationChannel(channel)
        }
    }

}