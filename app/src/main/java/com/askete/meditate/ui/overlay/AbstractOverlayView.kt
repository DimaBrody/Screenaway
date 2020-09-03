package com.askete.meditate.ui.overlay

import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.view.*
import android.widget.FrameLayout
import com.askete.meditate.App
import com.askete.meditate.R
import com.yandex.metrica.YandexMetrica


abstract class AbstractOverlayView(
    private val mContext: Context
) : FrameLayout(mContext) {

    protected val mainContext: Context
        get() = mContext.applicationContext

    private val windowManager: WindowManager
        get() = mainContext.getSystemService(Context.WINDOW_SERVICE)
                as WindowManager

    private var isVisible: Boolean = false

    protected var serviceIntent: Intent? = null

    private val windowParams: WindowManager.LayoutParams
        get() {
            val params: WindowManager.LayoutParams = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_FULLSCREEN or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                    -3
                )
                else -> WindowManager.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT, 2010, 67371020, -3
                )
            }
            params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)

            return params
        }


    private lateinit var mView: View


    open fun create() {
        mView = LayoutInflater.from(mainContext)
            .inflate(R.layout.fragment_overlay, null)

        App.handler!!.post { addView(mView) }

        setupView(mView)

        if (!isVisible) {
            try {
                App.handler!!.post {
                    windowManager.addView(this, windowParams)
                }
                isVisible = true
            } catch (e: Throwable){
//                YandexMetrica.reportEvent("Problem with creating overlay: ${e.message}")
            }

        }
    }

    abstract fun setupView(view: View)


    open fun dispose(time: Long = 250L) {
        mainContext.stopNonNull(serviceIntent)

        if (isVisible) {
            try {
                App.handler!!.postDelayed({
                    windowManager.removeView(this)
                }, time)
            } catch (e: Exception) {
            } finally {
                isVisible = false
            }
        }
    }

    private fun Context.stopNonNull(intent: Intent?) = intent?.let {
        stopService(it)
    }

}