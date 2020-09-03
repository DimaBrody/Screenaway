package com.askete.meditate.tools.database.repository

import androidx.lifecycle.LifecycleCoroutineScope
import com.askete.meditate.data.model.DataSnapshot

interface DatabaseRepository {

    fun insertToDatabase(data: DataSnapshot)

    fun fetchFromDatabase(
        lifecycle: LifecycleCoroutineScope,
        onLoadListener: (List<DataSnapshot>) -> Unit
    )
}