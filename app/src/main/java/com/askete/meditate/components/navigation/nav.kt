@file:JvmName("NavigationKt")

package com.askete.meditate.components.navigation

import com.askete.meditate.ui.charts.ChartsFragment
import com.askete.meditate.ui.countdown.CountDownFragment
import com.askete.meditate.ui.info.InfoFragment
import com.askete.meditate.ui.main.MainFragment
import com.askete.meditate.ui.preferences.PreferenceFragment
import com.askete.meditate.ui.splash.SplashFragment

fun navigateMain() {
    Navigation.navigate(MainFragment())
}

fun navigateInfo(){
    Navigation.navigate(InfoFragment())
}

fun navigateCountDown(){
    Navigation.navigate(CountDownFragment())
}

fun navigateSettings(){
    Navigation.navigate(PreferenceFragment())
}

fun navigateCharts(){
    Navigation.navigate(ChartsFragment())
}

fun navigateSplash(){
    Navigation.navigate(SplashFragment())
}