package com.askete.meditate.components.mvvm.viewmodel.delegates

import com.askete.meditate.components.mvvm.viewmodel.ViewModel
import com.askete.meditate.components.mvvm.viewmodel.ViewModelProvider
import kotlin.reflect.KClass

fun <V: ViewModel> viewModelDelegate(clazz: KClass<V>) =
    ViewModelProvider.ViewModelDelegate<V>(clazz)