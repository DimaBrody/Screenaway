package com.askete.meditate.ui.splash

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.askete.meditate.App
import com.askete.meditate.R
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.components.navigation.navigateMain
import com.askete.meditate.ui.base.BaseFragment
import com.askete.meditate.utils.*
import com.askete.meditate.utils.functions.RC_DISTURB
import com.askete.meditate.utils.functions.isNotificationAccessGranted
import com.askete.meditate.utils.functions.openNotDisturb
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.android.synthetic.main.fragment_splash.view.*
import kotlinx.android.synthetic.main.item_stepper.view.*

class SplashFragment : BaseFragment<SplashViewModel>(
    SplashViewModel::class
) {

    private val titleArray = arrayOf(
        "Productivity",
        "Autonomy",
        "Permissions"
    )
    private val descriptionArray = arrayOf(
        "Pay attention to really important tasks!",
        "Choose time you need to be safe from notifications and phone activity at all!",
        "Give suggested permissions to make an app work well!"
    )

    override fun View.createViews() {
        setLightStatusBar(requireActivity())
        bottomProgressDots(0)

        splash_viewpager.adapter = CustomPagerAdapter()
        splash_viewpager.addOnPageChangeListener(viewPagerPageChangeListener)

        splash_button.setOnClickListener {
            val current: Int = splash_viewpager.currentItem + 1
            if (current < MAX_STEP) {
                // move to next screen
                splash_viewpager.currentItem = current
            } else {
                if (overlayEnabled(requireContext())) {
                    if (isNotificationAccessGranted) {
                        navigateLightMain()
                    } else openNotDisturb()
                } else openOverlay()
            }
        }

        if (isOneMaxStep) {
            splash_button.apply {
                text = "Give Permissions"
                setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                setTextColor(Color.WHITE)
            }
        }
    }

    private fun View.bottomProgressDots(current_index: Int) {
        if (!isOneMaxStep) {
            val dots = arrayOfNulls<ImageView>(MAX_STEP)
            splash_dots.removeAllViews()
            for (i in dots.indices) {
                dots[i] = ImageView(requireContext())
                val widthHeight = 15
                val params =
                    LinearLayout.LayoutParams(ViewGroup.LayoutParams(widthHeight, widthHeight))
                params.setMargins(10, 10, 10, 10)
                dots[i]!!.layoutParams = params
                dots[i]!!.setImageResource(R.drawable.circle)
                dots[i]!!.setColorFilter(
                    resources.getColor(R.color.overlay_dark_30),
                    PorterDuff.Mode.SRC_IN
                )
                splash_dots.addView(dots[i])
            }
            if (dots.isNotEmpty()) {
                dots[current_index]!!.setImageResource(R.drawable.circle)
                dots[current_index]!!.setColorFilter(
                    resources.getColor(R.color.grey_10),
                    PorterDuff.Mode.SRC_IN
                )
            }
        } else splash_dots.visibility = View.GONE

    }

    companion object {
        private val MAX_STEP = if (prefs.isFirstLaunch) 3 else 1
    }

    var viewPagerPageChangeListener: OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageSelected(position: Int) {
            view?.bottomProgressDots(position)
            if (position == titleArray.size - 1 || isOneMaxStep) {
                splash_button.apply {
                    text = "Give Permissions"
                    setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
                    setTextColor(Color.WHITE)
                    if(prefs.isFirstLaunch) {
                        prefs.isFirstLaunch = false
                        if(App.debugMode){
                            mViewModel.insertDebugData()
                        }
                    }
                }
            } else {
                splash_button.apply {
                    text = "Next"
                    setBackgroundColor(resources.getColor(R.color.colorWhite))
                    setTextColor(resources.getColor(R.color.blue_grey_500))
                }
            }
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
        override fun onPageScrollStateChanged(arg0: Int) {}
    }


    private inner class CustomPagerAdapter : PagerAdapter() {
        private lateinit var mInflater: LayoutInflater

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            mInflater =
                requireActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            val view: View = layoutInflater.inflate(
                if (position != 2 && !isOneMaxStep) R.layout.item_stepper else R.layout.item_stepper_permissions,
                container,
                false
            )

            with(view) {
                title.text = titleArray[if (isOneMaxStep) 2 else position]
                description.text = descriptionArray[if (isOneMaxStep) 2 else position]
                if (!isOneMaxStep && (position == 0 || position == 1))
                    image.setImageResource(if (position == 0) R.drawable.img_wizard_1 else if (position == 1) R.drawable.img_wizard_2 else 0)


            }

//            (view.findViewById<View>(R.id.image) as ImageView).setImageResource(
//                about_images_array.get(position)
//            )


            container.addView(view)

            return view
        }

        override fun getCount(): Int = MAX_STEP

        override fun isViewFromObject(view: View, `object`: Any): Boolean = view == `object`

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            val view = `object` as View?
            container.removeView(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_OVERLAY) {
            if (isNotificationAccessGranted) {
                if (overlayEnabled(requireContext())) {
                    navigateLightMain()
                }
            } else openNotDisturb()
        }
        if (requestCode == RC_DISTURB) {
            if (overlayEnabled(requireContext()) && isNotificationAccessGranted) {
                navigateLightMain()
            }
        }
    }

    private val isOneMaxStep: Boolean
        get() = MAX_STEP == 1

    private fun navigateLightMain() {
//        YandexMetrica.reportEvent("SplashFragment","onPermissionTrue()")
        navigateMain()
        clearLightStatusBar(requireActivity())
    }


}