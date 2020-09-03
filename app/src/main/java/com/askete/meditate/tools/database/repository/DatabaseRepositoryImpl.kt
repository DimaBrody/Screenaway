package com.askete.meditate.tools.database.repository

import androidx.lifecycle.LifecycleCoroutineScope
import com.askete.meditate.data.model.DataSnapshot
import com.askete.meditate.tools.database.dao.DataDao
import com.askete.meditate.utils.thread.launchIO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class DatabaseRepositoryImpl(
    private val dao: DataDao
) : DatabaseRepository {

    override fun insertToDatabase(data: DataSnapshot) {
        GlobalScope.launch(Dispatchers.IO){
            dao.insertDataSnapshot(data)
        }
    }

    override fun fetchFromDatabase(
        lifecycle: LifecycleCoroutineScope,
        onLoadListener: (List<DataSnapshot>) -> Unit
    ) {
        lifecycle.launchIO {
            val data = dao.fetchAllData()
            onLoadListener(data)
        }
    }
}