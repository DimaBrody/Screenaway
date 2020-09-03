package com.askete.meditate.tools.database.dao

import androidx.room.*
import com.askete.meditate.data.model.DataSnapshot

@Dao
interface DataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertDataSnapshot(data: DataSnapshot)

    @Query("SELECT * FROM DataSnapshot")
    fun fetchAllData(): List<DataSnapshot>

}