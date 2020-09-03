package com.askete.meditate.components.navigation

import android.util.Log
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.askete.meditate.R
import com.askete.meditate.data.constants.Constants.REPEAT_KEY
import com.askete.meditate.ui.main.MainFragment

object Navigation {
    private const val NO_INIT = "Write Navigation." +
            "init(this) in your activity!"

    private var activity: FragmentActivity? = null

    private val transactionAnimations =
        TransactionAnimations.create()

    private var container: Int = 0

    fun init(
        activity: AppCompatActivity,
        container: Int,
        onSplash: (() -> Fragment)? = null,
        onRepeat: (() -> Fragment)? = null
    ) {
        Navigation.activity = activity
        Navigation.container = container

        if (activity.intent != null && activity.intent
                .getBooleanExtra(REPEAT_KEY, false)) {
            safeRepeat(onRepeat)
        } else safeSplash(onSplash)
    }

    private fun safeSplash(onSplash: (() -> Fragment)?) {
        onSplash?.let { navigate(it.invoke()) }
    }

    private fun safeRepeat(onRepeat: (() -> Fragment)?) {
        onRepeat?.let { navigate(it.invoke()) }
    }

    fun navigate(
        fragment: Fragment,
        isAnimate: Boolean = true
    ) = activity?.supportFragmentManager?.apply {
        val transition = if (isAnimate) beginAnimatedTransaction {
            enter = R.anim.nav_default_enter_anim
            exit = R.anim.nav_default_exit_anim
            popEnter = R.anim.nav_default_pop_enter_anim
            popExit = R.anim.nav_default_pop_exit_anim
        } else beginTransaction()

        transition.replace(container, fragment, fragment.name())
            .commitAllowingStateLoss()
    } ?: also {
        if (activity == null)
            Log.e(name(), NO_INIT)
    }

    fun processBack(onSuccess: (Fragment) -> Unit): Boolean = activity?.let {
        with(it.supportFragmentManager) {
            val fragment = findFragmentById(container)
            if (fragment !is MainFragment && fragment != null) {
                onSuccess(fragment)
                true
            } else false
        }
    } ?: false

    private fun setNavigationLifecycleObserver(
        owner: LifecycleOwner
    ) {
        owner.lifecycle.addObserver(NavigationLifecycleObserver())
    }

    private fun FragmentManager.beginAnimatedTransaction(
        onSetAnimations: TransactionAnimations.() -> Unit
    ): FragmentTransaction {
        onSetAnimations(transactionAnimations)
        return with(transactionAnimations) {
            beginTransaction().setCustomAnimations(
                enter.normal(),
                exit.normal(),
                popEnter.normal(),
                popExit.normal()
            )
        }
    }

    private class TransactionAnimations {
        @AnimRes
        @AnimatorRes
        var enter: Int? = null

        @AnimRes
        @AnimatorRes
        var exit: Int? = null

        @AnimRes
        @AnimatorRes
        var popEnter: Int? = null

        @AnimRes
        @AnimatorRes
        var popExit: Int? = null

        companion object {
            fun create(): TransactionAnimations =
                TransactionAnimations()
        }
    }

    private class NavigationLifecycleObserver : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
//            if (event == Lifecycle.Event.ON_DESTROY)
//                activity = null
        }
    }


    private fun Int?.normal() = this ?: 0

//    fun navigateOverlay(cleanType: CleanType,items: List<CleanItem>) =
//        Navigation.navigate(OverlayFragment.newInstance(cleanType,items))

    fun Any?.name() = this?.javaClass?.simpleName ?: "null"
}