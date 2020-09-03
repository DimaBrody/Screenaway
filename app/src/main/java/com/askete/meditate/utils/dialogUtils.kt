package com.askete.meditate.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import com.askete.meditate.R
import com.askete.meditate.components.dependencies.prefs
import java.util.concurrent.TimeUnit


fun createDialog(context: Context, dialogData: MutableLiveData<DialogTimeData>) {
    val builder = AlertDialog.Builder(context).apply {
        setTitle("Select Time")
        val types = context.resources.getStringArray(R.array.time_array)

        val currentId = prefs.selectedTimeId

        types[currentId] = "${types[currentId]}  ~"

        setItems(types) { _, which ->
            dialogData.value = DialogTimeData(which, getDialogNameById(which))
        }
    }
    builder.show()
}

private fun getDialogNameById(id: Int) = when (id) {
    0 -> "20 minutes"
    1 -> "30 minutes"
    2 -> "40 minutes"
    3 -> "60 minutes"
    4 -> "90 minutes"
    else -> ""
}

fun getTimeById(id: Int): Long = TimeUnit.MINUTES.toMillis(
    when (id) {
        0 -> 20
        1 -> 30
        2 -> 40
        3 -> 60
        4 -> 90
        else -> 0
    }
)

data class DialogTimeData(
    val id: Int,
    val text: String
) {
    companion object {
        fun createDefault() = DialogTimeData(0, "20 minutes")

        fun createSaved() = DialogTimeData(
            prefs.selectedTimeId,
            getDialogNameById(prefs.selectedTimeId)
        )
    }
}