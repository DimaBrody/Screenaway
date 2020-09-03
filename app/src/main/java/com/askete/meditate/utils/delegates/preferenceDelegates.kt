package com.askete.meditate.utils.delegates

import android.content.SharedPreferences
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun SharedPreferences.string(
    defaultValue: String = "",
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, String?> =
    PreferenceProperty(
        this,
        defaultValue,
        key
    )

fun SharedPreferences.long(
    defaultValue: Long = 0L,
    key: (KProperty<*>) -> String = KProperty<*>::name
): ReadWriteProperty<Any, Long> =
    PreferenceProperty(
        this,
        defaultValue,
        key
    )

fun SharedPreferences.float(
    defaultValue: Float = 0f,
    key: (KProperty<*>) -> String = KProperty<*>::name
) : ReadWriteProperty<Any, Float> =
    PreferenceProperty(
        this,
        defaultValue,
        key
    )

fun SharedPreferences.int(
    defaultValue: Int = 0,
    key: (KProperty<*>) -> String = KProperty<*>::name
) : ReadWriteProperty<Any, Int> =
    PreferenceProperty(
        this,
        defaultValue,
        key
    )

fun SharedPreferences.boolean(
    defaultValue: Boolean = false,
    key: (KProperty<*>) -> String = KProperty<*>::name,
    onSetCallback: ((Boolean) -> Unit)? = null
) : ReadWriteProperty<Any, Boolean> =
    PreferenceProperty(
        this,
        defaultValue,
        key,onSetCallback
    )

@Suppress("UNCHECKED_CAST")
private class PreferenceProperty<T>(
    private val prefs: SharedPreferences,
    private val defaultValue: T,
    private val key: (KProperty<*>) -> String = KProperty<*>::name,
    private val onSetCallback: ((T) -> Unit)? = null
) : ReadWriteProperty<Any, T> {

    override fun getValue(thisRef: Any, property: KProperty<*>): T =
        with(prefs) {
            when (defaultValue) {
                is String? -> getString(key(property), defaultValue) as T
                is Long -> getLong(key(property), defaultValue) as T
                is Float -> getFloat(key(property), defaultValue) as T
                is Int -> getInt(key(property), defaultValue) as T
                is Boolean -> getBoolean(key(property), defaultValue) as T
                else -> throw IllegalArgumentException("Wrong type for preferences.")
            }
        }


    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) = with(prefs) {
        onSetCallback?.invoke(value)
        edit().apply {
            when (value) {
                is String? -> putString(key(property), value)
                is Long -> putLong(key(property), value)
                is Float -> putFloat(key(property), value)
                is Int -> putInt(key(property), value)
                is Boolean -> putBoolean(key(property), value)
            }
        }.apply()
    }
}