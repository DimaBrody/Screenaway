package com.askete.meditate.ui.main

import android.view.View
import com.askete.meditate.R
import com.askete.meditate.components.navigation.navigateCharts
import com.askete.meditate.components.navigation.navigateInfo
import com.askete.meditate.components.navigation.navigateSettings
import com.askete.meditate.ui.base.BaseFragment
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_main.view.*

class MainFragment : BaseFragment<MainViewModel>(
    MainViewModel::class
), View.OnClickListener {

    override fun View.createViews() {
        main_spinner.init(main_spinner_text)
        main_button_proceed.setOnClickListener(this@MainFragment)
        main_button_settings.setOnClickListener(this@MainFragment)
        main_button_charts.setOnClickListener(this@MainFragment)

//        YandexMetrica.reportEvent("MainFragment","onStart()")
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.main_button_proceed -> {
//                if (overlayEnabled(requireContext())) {
//                    if (isNotificationAccessGranted)
//                        navigateInfo()
//                    else openNotDisturb()
//                } else openOverlay()
                navigateInfo()
            }
            R.id.main_button_settings -> {
                navigateSettings()
            }
            R.id.main_button_charts -> {
                navigateCharts()
            }
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == RC_OVERLAY) {
//            if (isNotificationAccessGranted) {
//                if (overlayEnabled(requireContext())) {
//                    navigateInfo()
//                }
//            } else openNotDisturb()
//        }
//        if (requestCode == RC_DISTURB) {
//            if (overlayEnabled(requireContext()) && isNotificationAccessGranted) {
//                navigateInfo()
//            }
//        }
//    }

}