@file:Suppress("UNCHECKED_CAST")

package com.askete.meditate.components.mvvm.viewmodel

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.askete.meditate.components.mvvm.factory.Factory
import com.askete.meditate.components.mvvm.fragment.ViewModelFragment
import com.askete.meditate.components.mvvm.fragment.ViewModelStore
import java.lang.Exception
import java.lang.IllegalArgumentException
import java.lang.NullPointerException
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object ViewModelProvider {

    private const val KEY_PREFIX = "viewModel."

    private var mFactory: Factory? = null

    private var mViewModelStore: ViewModelStore? = null

    fun set(factory: Factory) {
        mFactory = factory
    }

    class ViewModelDelegate<V: ViewModel>(
        private val clazz: KClass<V>
    ) : ReadOnlyProperty<ViewModelFragment,V> {
        override fun getValue(thisRef: ViewModelFragment, property: KProperty<*>): V {
            connectWithStore(thisRef)
            if(isAvailableFactory()){
                val modelClass = clazz.java
                val name = KEY_PREFIX + modelClass.canonicalName
                return get(name,modelClass)
            } else throw IllegalArgumentException("ViewModelStore is null.")
        }
    }

    private fun connectWithStore(fragment: ViewModelFragment) {
        if (!isAvailableFragment(fragment)) {
            mViewModelStore = ViewModelStore()
            try {
                throw NullPointerException("Activity of Application is null!")
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
        } else {
            mViewModelStore = fragment.store
            setupFragmentLifecycle(fragment)
        }
    }

    private fun isAvailableFragment(fragment: Fragment): Boolean {
        val activity = fragment.activity
        val application = activity?.application

        return activity != null && application != null
    }

    private fun setupFragmentLifecycle(fragment: ViewModelFragment) {
        fragment.lifecycle.addObserver(ViewModelObserver(fragment))
    }

    private fun isAvailableFactory() : Boolean {
        if(mFactory == null)
            mFactory = DefaultFactory()
        return mViewModelStore != null
    }

    private fun <V: ViewModel> get(key: String?,modelClass: Class<V>) : V {
        if(key == KEY_PREFIX || key == null)
            throw NullPointerException("Canonical name is null.")
        var viewModel = mViewModelStore!![key]
        return if(modelClass.isInstance(viewModel))
            viewModel as V else {
            viewModel = mFactory!!.create(modelClass)
            viewModel.also { mViewModelStore!![key] = it }
        }
    }

    private class DefaultFactory: Factory {
        override fun <F> create(modelClass: Class<F>): F {
            try {
                return modelClass.newInstance()
            } catch (e: Exception) {
                throw IllegalArgumentException("You cannot use arguments in constructor " +
                        "of viewModel without according factory.")
            }
        }
    }

    private class ViewModelObserver(
        private val fragment: ViewModelFragment
    ) : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                fragment.store.clear()
                mViewModelStore = null
            }
        }

    }

}