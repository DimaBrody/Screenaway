package com.askete.meditate.ui

import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.askete.meditate.R
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.navigation.Navigation
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.tools.receivers.BootReceiver
import com.askete.meditate.ui.countdown.CountDownFragment
import com.askete.meditate.ui.info.InfoFragment
import com.askete.meditate.ui.main.MainFragment
import com.askete.meditate.ui.splash.SplashFragment
import com.askete.meditate.utils.clearLightStatusBar
import com.askete.meditate.utils.functions.isNotificationAccessGranted
import com.askete.meditate.utils.functions.simpleTry
import com.askete.meditate.utils.getMainImageWithPrefs
import com.askete.meditate.utils.overlayEnabled
import com.askete.meditate.utils.setupTranslucentApp
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setupTranslucentApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!prefs.isEnd){
            val onBootReceiver =
                ComponentName(application.packageName, BootReceiver::class.java.name)
            if (packageManager.getComponentEnabledSetting(onBootReceiver) != PackageManager.COMPONENT_ENABLED_STATE_ENABLED) packageManager.setComponentEnabledSetting(
                onBootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        activity = this

        main_container.background = ContextCompat.getDrawable(this, getMainImageWithPrefs(prefs))

        Navigation.init(this, R.id.main_host, onMainFragment, onRepeatFragment)
    }

    private val onMainFragment: () -> Fragment
        get() = {
            if (overlayEnabled(this) && isNotificationAccessGranted)
                MainFragment() else SplashFragment()
        }

    private val onRepeatFragment: () -> Fragment
        get() = { CountDownFragment() }

    override fun onBackPressed() {
        if (!Navigation.processBack {
                if (it !is InfoFragment && it !is CountDownFragment)
                    clearLightStatusBar(this)
                navigateMain()
            }) {
            finish()
        }
    }


    companion object {
        var activity: MainActivity? = null
            private set

        fun finishActivity() = simpleTry {
            activity?.finish()

        }
    }
}
