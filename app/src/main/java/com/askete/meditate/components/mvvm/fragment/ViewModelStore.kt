package com.askete.meditate.components.mvvm.fragment

import com.askete.meditate.components.mvvm.viewmodel.ViewModel

class ViewModelStore {

    private val mStore = hashMapOf<String, ViewModel>()

    operator fun get(name: String) = mStore[name]

    operator fun set(name: String,viewModel: ViewModel){
        mStore[name] = viewModel
    }

    fun clear() {
        for (vm in mStore.values)
            vm.clear()
    }
}