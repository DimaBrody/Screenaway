package com.askete.meditate.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.askete.meditate.R
import com.askete.meditate.components.mvvm.fragment.ViewModelFragment
import com.askete.meditate.ui.MainActivity
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.CHARTS
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.COUNTDOWN
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.INFO
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.MAIN
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.SETTINGS
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.SPLASH

abstract class BaseSimpleFragment: ViewModelFragment() {

    private val mFragmentType: Int
        get() = getFragmentType()

    protected val mainActivity: MainActivity?
        get() = (activity as? MainActivity?)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(layoutID,container,false)

        view.createViews()

        return view
    }

    private val layoutID: Int
        get() = when (mFragmentType) {
            MAIN -> R.layout.fragment_main
            INFO -> R.layout.fragment_info
            COUNTDOWN -> R.layout.fragment_countdown
            SETTINGS -> R.layout.fragment_preferences
            CHARTS -> R.layout.fragment_charts
            SPLASH -> R.layout.fragment_splash

            else -> throw IllegalArgumentException(
                "No Layout for current fragment found!"
            )
        }


    abstract fun View.createViews()

    abstract fun getFragmentType() : Int

    protected object FragmentType {
        const val MAIN = 0x1
        const val INFO = 0x2
        const val COUNTDOWN = 0x3
        const val SETTINGS = 0x4
        const val CHARTS = 0x5
        const val SPLASH = 0x6

        const val UNDEFINED = 0xF

    }
}