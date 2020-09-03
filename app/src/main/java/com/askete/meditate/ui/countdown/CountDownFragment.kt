package com.askete.meditate.ui.countdown

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.askete.meditate.App
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.tools.services.OverlayService
import com.askete.meditate.ui.base.BaseSimpleFragment
import kotlinx.android.synthetic.main.fragment_countdown.view.*
import kotlin.math.roundToInt


class CountDownFragment : BaseSimpleFragment() {

    private lateinit var countDownTimer: CountDownTimer

    override fun View.createViews() {
        setupObservers()
        countdown_back.setOnClickListener { navigateMain() }
        countdown_text.text = "3"

        countDownTimer = createCountdownTimer(countdown_text) {
            val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

            mainActivity!!.window.decorView.systemUiVisibility = flags;
            val  decorView = mainActivity!!.window.decorView;
            decorView.setOnSystemUiVisibilityChangeListener {
                if (it and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
            requireContext().startService(Intent(requireContext(), OverlayService::class.java))
            App.handler!!.postDelayed({
                navigateMain()
            },500)
        }

    }

    private fun setupObservers() {
        lifecycle.addObserver(object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_DESTROY) {
                    countDownTimer.cancel()
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        countDownTimer.start()
    }

    private fun createCountdownTimer(textView: TextView, onFinish: () -> Unit): CountDownTimer =
        object : CountDownTimer(
            ALL_TIME,
            INTERVAL_TIME
        ) {
            override fun onFinish() {
                onFinish()
            }

            override fun onTick(millisUntilFinished: Long) {
                textView.text = "${(millisUntilFinished / 1000f).roundToInt()}"
            }

        }


    override fun getFragmentType(): Int = FragmentType.COUNTDOWN

    companion object {
        private const val ALL_TIME = 3000L
        private const val INTERVAL_TIME = 1000L
    }

}