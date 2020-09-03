package com.askete.meditate.utils.delegates

import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import java.io.Serializable
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun <T : Any> argument(): ReadWriteProperty<Fragment, T> =
    FragmentArgumentDelegate()

@Suppress("UNCHECKED_CAST")
class FragmentArgumentDelegate<T : Any> :
    ReadWriteProperty<Fragment, T> {
    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        val key = property.name
        return thisRef.arguments?.get(key) as T
    }


    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        val args = thisRef.arguments ?: Bundle().also(thisRef::setArguments)
        val key = property.name
        args.put(key,value)
    }

}

private fun <T> Bundle.put(key: String, value: T) = when (value) {
    is Boolean -> putBoolean(key, value)
    is String -> putString(key, value)
    is Int -> putInt(key, value)
    is Float -> putFloat(key, value)
    is Serializable -> putSerializable(key, value)
    is Parcelable -> putParcelable(key, value)
    else -> throw IllegalArgumentException("Wrong type in delegate fragment.")
}