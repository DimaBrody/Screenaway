package com.askete.meditate.utils

import android.widget.ImageView
import com.askete.meditate.R
import com.askete.meditate.tools.prefs.PreferenceProvider
import com.askete.meditate.utils.animations.fadeIn


fun ImageView.imageViewAnimatedChange(animatedImageView: ImageView, newImage: Int) {
    animatedImageView.setImageResource(newImage)
    animatedImageView.fadeIn(1000) {
        setImageResource(newImage)
        animatedImageView.alpha = 0f
    }
}

fun getMainImageWithPrefs(prefs: PreferenceProvider): Int {
    return when (prefs.currentBackgroundMain) {
        0 -> R.drawable.background_1
        1 -> R.drawable.background_2
        2 -> R.drawable.background_3
        3 -> R.drawable.background_4
        else -> R.drawable.background_1
    }.also {

        if (prefs.currentBackgroundMain == 3) prefs.currentBackgroundMain =
            0 else prefs.currentBackgroundMain++

    }
}

fun getOverlayImageWithPrefs(prefs: PreferenceProvider): Int {
    return when (prefs.currentBackgroundMain) {
        1 -> R.drawable.background_default_1_hq
        2 -> R.drawable.background_default_2_hq
        3 -> R.drawable.background_default_3_hq
        0 -> R.drawable.background_default_4_hq
        else -> R.drawable.background_default_1_hq
    }
}

fun getOverlayImageWithId(id: Int): Int {
    return when (id) {
        1 -> R.drawable.background_default_1_hq
        2 -> R.drawable.background_default_2_hq
        3 -> R.drawable.background_default_3_hq
        0 -> R.drawable.background_default_4_hq
        else -> R.drawable.background_default_1_hq
    }
}


