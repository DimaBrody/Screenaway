package com.askete.meditate.ui.splash

import com.askete.meditate.components.mvvm.viewmodel.ViewModel
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.database.repository.DatabaseRepository

class SplashViewModel(
    private val databaseRepository: DatabaseRepository
) : ViewModel(){

    fun insertDebugData(){
        DataSnapshot.createTestData().forEach {
            databaseRepository.insertToDatabase(it)
        }
    }

}