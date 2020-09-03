package com.askete.meditate.components.factory

import com.askete.meditate.components.mvvm.factory.Factory
import com.askete.meditate.tools.database.repository.DatabaseRepository
import com.askete.meditate.ui.charts.ChartsViewModel
import com.askete.meditate.ui.main.MainViewModel
import com.askete.meditate.ui.splash.SplashViewModel
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val databaseRepository: DatabaseRepository
) : Factory {

    override fun <F> create(modelClass: Class<F>): F = with(modelClass){
        when {
            isAssignableFrom(MainViewModel::class.java)->MainViewModel() as F
            isAssignableFrom(ChartsViewModel::class.java)->ChartsViewModel(
                databaseRepository
            ) as F
            isAssignableFrom(SplashViewModel::class.java)->SplashViewModel(
                databaseRepository
            ) as F
            else-> throw IllegalArgumentException("ViewModel is null.")
        }
    }

}