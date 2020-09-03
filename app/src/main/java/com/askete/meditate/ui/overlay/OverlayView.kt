package com.askete.meditate.ui.overlay

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Handler
import android.os.IBinder
import android.view.View
import com.askete.meditate.R
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.dependencies.repository
import com.askete.meditate.data.constants.Constants.REPEAT_KEY
import com.askete.meditate.tools.services.OverlayService
import com.askete.meditate.ui.MainActivity
import com.askete.meditate.ui.overlay.runnables.NotificationRunnable
import com.askete.meditate.utils.*
import com.askete.meditate.utils.animations.fadeFromTo
import com.askete.meditate.utils.animations.fadeIn
import com.askete.meditate.utils.animations.fadeOut
import com.askete.meditate.utils.functions.setupAudioDefault
import com.askete.meditate.utils.functions.setupAudioSilent
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_overlay.view.*
import kotlinx.android.synthetic.main.layout_finish.view.*
import kotlinx.android.synthetic.main.layout_pay.view.*
import java.util.concurrent.TimeUnit


class OverlayView(context: Context) : AbstractOverlayView(context) {

    private lateinit var mConnection: ServiceConnection
    private lateinit var mNotificationHandler: Handler
    private var mService: OverlayService? = null
    private var mBound: Boolean = false

    private var imageID: Int = -1

    private var duration: Long = 0L
        get() = field / 1000

    override fun setupView(view: View) {
        view.setupConnection()
        view.setupDefaultViews()
    }

    private fun View.setupDefaultViews() {
        overlay_time.text = getTimeFromTicks(duration)
        overlay_exit.setOnClickListener {
            overlay_layout_pay.fadeIn()
            overlay_exit.fadeFromTo(0.3f, 0f)
        }
        pay_exit.setOnClickListener {
            overlay_layout_pay.fadeOut {
                overlay_layout_pay.visibility = View.GONE
            }
            overlay_exit.fadeFromTo(0f, 0.3f)
        }
        pay_left.setOnClickListener {
            mService?.leftFromOverlay()
        }

        imageID = prefs.currentBackgroundMain

        overlay_image_background.setImageResource(getOverlayImageWithPrefs(prefs))
    }

    fun setupOverlay(duration: Long): OverlayView = apply {
        this.duration = duration
    }

    private fun View.setupConnection() {
        setupAudioSilent(context)
        startCollapsing()

        mConnection = object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                mBound = false
                mService = null
                context.unbindService(mConnection)
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                mBound = true
                mService = (service as OverlayService.OverlayBinder).service
                mService?.setOnTimeListener {
                    if (it <= 0L) {
                        if (it == -1L) {
                            mService?.let { service ->
                                service.databaseData?.let { databaseData ->
                                    repository.insertToDatabase(databaseData.apply {
                                        isLeft = true
                                        leftTime = System.currentTimeMillis()
                                    })
                                }
                            }
                        }
                        finishOverlay(it != -1L)
                    }
                    if (it != 0L && it % 60 == 0L && it != duration) {
                        if (++imageID > 3)
                            imageID = 0
                        overlay_image_background.imageViewAnimatedChange(
                            overlay_image_background_animate,
                            getOverlayImageWithId(imageID)
                        )
                    }
                    overlay_time?.let { textView ->
                        textView.text = if (it != -1L) getTimeFromTicks(it) else "00:00"
                    }
                }
            }

        }

        val intent = Intent(context, OverlayService::class.java)
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        setOnTestActionListener {
            finishOverlay(true)
            mService?.cancelTimer()
        }

    }

    @SuppressLint("StringFormatMatches")
    private fun View.finishOverlay(isFinishShow: Boolean) {
        setupAudioDefault(context)
        mBound = false

//        YandexMetrica.reportEvent("OverlayService","onFinish()")

        prefs.isEnd = true

        finish_desc.text = context.resources.getString(
            R.string.overlay_finish_desc,
            TimeUnit.SECONDS.toMinutes(duration)
        )
        

        if (isFinishShow)
            overlay_layout_finish.fadeIn {
                finish_button_exit.setOnClickListener {
                    MainActivity.finishActivity()
                    dispose(0)
//                disposeOverlay()
                }
                finish_button_close.setOnClickListener {
                    MainActivity.finishActivity()
                    dispose(0)
//                disposeOverlay()
                }
                finish_button_again.setOnClickListener {
                    disposeOverlay()
                    startMain()
                }
            } else {
            disposeOverlay()
            startMain(false)
        }
    }

    private fun View.disposeOverlay() {
        dispose()
        overlay_main.fadeOut()
    }

    private fun startMain(isRepeat: Boolean = true) = with(mainContext) {
        prefs.isEnd = true
        mService?.stopSelf()

        val intent = packageManager.getLaunchIntentForPackage(packageName)
        intent?.apply {
            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(REPEAT_KEY, isRepeat)
            startActivity(intent)
        }
    }

    @SuppressLint("WrongConstant")
    private fun startCollapsing() {
        if (!::mNotificationHandler.isInitialized)
            mNotificationHandler = Handler()

        mNotificationHandler.postDelayed(NotificationRunnable(context) {
            if (mBound)
                mNotificationHandler.postDelayed(this, 100)
        }, 300)
    }


    companion object {

        var onTestActionListener: OnTestAction? = null

        private fun setOnTestActionListener(onTest: () -> Unit) {
            onTestActionListener =
                object : OnTestAction {
                    override fun onTest() {
                        onTest()
                    }
                }

        }

        interface OnTestAction {
            fun onTest()
        }
    }

}