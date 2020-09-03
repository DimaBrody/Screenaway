package com.askete.meditate.tools.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.dependencies.repository
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.ui.overlay.OverlayView
import com.askete.meditate.utils.getTimeById
import com.yandex.metrica.YandexMetrica
import kotlin.math.roundToLong

class OverlayService : Service() {

    private lateinit var mOverlayView: OverlayView

    private lateinit var mCountDownTimer: CountDownTimer

     var databaseData: DataSnapshot? = null

    private val selectedTime: Long
        get() = getTimeById(prefs.selectedTimeId)

    var onTimeListener: OnTimeListener? = null


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mOverlayView = OverlayView(applicationContext).setupOverlay(selectedTime)

        if (prefs.isEnd) {
            databaseData = DataSnapshot(
                System.currentTimeMillis(),
                prefs.selectedTimeId
            )
            prefs.currentTime = databaseData!!.date
            repository.insertToDatabase(databaseData!!)
        }

        prefs.isEnd = false

        mCountDownTimer = object : CountDownTimer(selectedTime, 1000L) {
            override fun onFinish() {
                onTimeListener?.onTick(0)
                stopSelf()
            }

            override fun onTick(millisUntilFinished: Long) {
                onTimeListener?.onTick((millisUntilFinished / 1000f).roundToLong())
            }
        }

        mOverlayView.create()
        mCountDownTimer.start()

//        YandexMetrica.reportEvent(
//            "[ OverlayService ] dataSnapshot = (timeId = ${databaseData?.timeId}, timeDate = ${databaseData?.date})",
//            "onStart()"
//        )



        return super.onStartCommand(intent, flags, startId)
    }

    fun leftFromOverlay() {
        onTimeListener?.onTick(-1)
        prefs.isEnd = true
        stopSelf()
        cancelTimer()
    }

    fun cancelTimer() = mCountDownTimer.cancel()

    override fun onBind(intent: Intent?): IBinder? = OverlayBinder()

    inner class OverlayBinder : Binder() {
        val service: OverlayService
            get() = this@OverlayService
    }

    inline fun setOnTimeListener(crossinline callback: (Long) -> Unit) {
        onTimeListener = object :
            OnTimeListener {
            override fun onTick(time: Long) {
                callback(time)
            }
        }
    }

    interface OnTimeListener {
        fun onTick(time: Long)
    }
}