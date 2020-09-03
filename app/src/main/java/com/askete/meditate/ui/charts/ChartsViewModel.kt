package com.askete.meditate.ui.charts

import androidx.lifecycle.LifecycleCoroutineScope
import com.askete.meditate.components.mvvm.viewmodel.ViewModel
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.database.repository.DatabaseRepository

class ChartsViewModel(
    private val databaseRepository: DatabaseRepository
) : ViewModel() {
    fun fetchData(
        lifecycleCoroutineScope: LifecycleCoroutineScope,
        onLoadListener: (List<DataSnapshot>) -> Unit
    ) {
        databaseRepository.fetchFromDatabase(lifecycleCoroutineScope,onLoadListener)
    }

}