package com.askete.meditate.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.askete.meditate.utils.randomCalendarDate
import com.askete.meditate.utils.randomInt

@Entity
data class DataSnapshot(
    @PrimaryKey(autoGenerate = false)
    val date: Long = -1,
    val timeId: Int = -1,
    val desc: String = "",
    var isLeft: Boolean = false,
    var leftTime: Long = -1
){
    companion object {
        fun createTestData() : List<DataSnapshot> =
            listOf(
                DataSnapshot(randomCalendarDate, randomInt(0,4)),
                DataSnapshot(randomCalendarDate, randomInt(0,4)),
                DataSnapshot(randomCalendarDate, randomInt(0,4))
            )

        fun createDescOnlyData(desc: String) : DataSnapshot = DataSnapshot(desc = desc)
    }
}