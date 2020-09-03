package com.askete.meditate.ui.dialog

import android.app.TimePickerDialog
import android.content.Context

class DialogPicker(ctx: Context, hour: Int, minuteOfDay: Int,onTimeListener: (Int, Int) -> Unit) : TimePickerDialog(ctx,
    OnTimeSetListener { _, hourOfDay, minute ->
        onTimeListener(hourOfDay, minute)
    },hour,minuteOfDay,false)