package com.askete.meditate.ui.views

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.MutableLiveData
import com.askete.meditate.components.dependencies.prefs
import com.askete.meditate.utils.DialogTimeData
import com.askete.meditate.utils.createDialog

class SpinnerLayout(context: Context, attrsSet: AttributeSet) :
    ConstraintLayout(context, attrsSet) {

    private var dialogData = MutableLiveData(DialogTimeData.createSaved())

    fun init(textView: TextView) {
        dialogData.observeForever {
            prefs.selectedTimeId = it.id
            textView.text = it.text
        }

        setOnClickListener { createDialog(context, dialogData) }
    }
}