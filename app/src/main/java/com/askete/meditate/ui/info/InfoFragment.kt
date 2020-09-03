package com.askete.meditate.ui.info

import android.view.View
import com.askete.meditate.components.navigation.navigateCountDown
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.ui.base.BaseSimpleFragment
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_info.view.*

class InfoFragment : BaseSimpleFragment() {

    override fun View.createViews() {
        info_back.setOnClickListener {
            navigateMain()
        }
        info_confirm_button.setOnClickListener {
            navigateCountDown()
        }
//        YandexMetrica.reportEvent("InfoFragment","onStart()")
    }

    override fun getFragmentType(): Int = FragmentType.INFO

}