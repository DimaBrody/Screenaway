package com.askete.meditate.components.mvvm.fragment.interfaces

import com.askete.meditate.components.mvvm.viewmodel.ViewModel
import kotlin.reflect.KClass

interface HasViewModel<VM: ViewModel> {
    val mViewModel: VM

    val mViewModelClass: KClass<VM>
}