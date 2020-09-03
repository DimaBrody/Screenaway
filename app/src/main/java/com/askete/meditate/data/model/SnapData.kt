package com.askete.meditate.data.model

data class SnapData(
    val header: String,
    val desc: String
){
    companion object {
        fun createTestData() : List<SnapData> =
            listOf(
                SnapData(Desc.TOTAL_DAYS,"4"),
                SnapData(Desc.COMPLETED_TIMES,"15"),
                SnapData(Desc.LEAVES,"1"),
                SnapData(Desc.CURRENT_ROW,"3"),
                SnapData(Desc.BEST_ROW,"5")
            )

        fun createEmptyData() : List<SnapData> =
            listOf(
                SnapData(Desc.TOTAL_DAYS,"-"),
                SnapData(Desc.COMPLETED_TIMES,"-"),
                SnapData(Desc.LEAVES,"-"),
                SnapData(Desc.CURRENT_ROW,"-"),
                SnapData(Desc.BEST_ROW,"-")
            )



    }

    object Desc {
        const val TOTAL_DAYS = "Total Days (used)"
        const val COMPLETED_TIMES = "Completed (times)"
        const val LEAVES = "Leaves (times)"
        const val CURRENT_ROW = "Current days in a row"
        const val BEST_ROW = "Best days in a row"
    }
}