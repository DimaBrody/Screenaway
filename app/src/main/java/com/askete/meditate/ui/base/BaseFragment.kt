package com.askete.meditate.ui.base

import com.askete.meditate.components.mvvm.fragment.interfaces.HasViewModel
import com.askete.meditate.components.mvvm.viewmodel.ViewModel
import com.askete.meditate.components.mvvm.viewmodel.delegates.viewModelDelegate
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.CHARTS
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.MAIN
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.SPLASH
import com.askete.meditate.ui.base.BaseSimpleFragment.FragmentType.UNDEFINED
import com.askete.meditate.ui.charts.ChartsViewModel
import com.askete.meditate.ui.main.MainViewModel
import com.askete.meditate.ui.splash.SplashViewModel
import kotlin.reflect.KClass

abstract class BaseFragment<VM : ViewModel>(
    final override val mViewModelClass: KClass<VM>
) : BaseSimpleFragment(), HasViewModel<VM> {

    override val mViewModel: VM by viewModelDelegate(mViewModelClass)

    override fun getFragmentType(): Int {
        return with(mViewModelClass.java){
            when {
                isAssignableFrom(MainViewModel::class.java) -> MAIN
                isAssignableFrom(ChartsViewModel::class.java) -> CHARTS
                isAssignableFrom(SplashViewModel::class.java) -> SPLASH
                else-> UNDEFINED
            }
        }
    }

}