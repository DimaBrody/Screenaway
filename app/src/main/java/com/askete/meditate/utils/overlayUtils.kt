package com.askete.meditate.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.fragment.app.Fragment

const val RC_OVERLAY = 111

fun Fragment.openOverlay() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val uri = Uri.parse("package:" + context?.packageName)
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
        startActivityForResult(intent, RC_OVERLAY)
    }
}

fun overlayEnabled(context: Context): Boolean =
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
        true else Settings.canDrawOverlays(context)