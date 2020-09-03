package com.askete.meditate.ui.overlay.runnables

import android.annotation.SuppressLint
import android.content.Context
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
class NotificationRunnable(
    private val context: Context,
    private val onBoundChecker: NotificationRunnable.() -> Unit
) : Runnable {
    @SuppressLint("WrongConstant")
    override fun run() {
        val statusBarService = context.getSystemService("statusbar")
        var statusBarManager: Class<*>? = null

        try {
            statusBarManager = Class.forName("android.app.StatusBarManager")
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }

        var collapseStatusBarMethod: Method? = null

        try {
            collapseStatusBarMethod = statusBarManager!!.getMethod("collapsePanels")
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }

        collapseStatusBarMethod?.isAccessible = true

        try {
            collapseStatusBarMethod?.invoke(statusBarService)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }

        onBoundChecker()
    }

}