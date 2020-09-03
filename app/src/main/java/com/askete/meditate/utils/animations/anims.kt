package com.askete.meditate.utils.animations

import android.animation.Animator
import android.animation.ValueAnimator
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import java.lang.IllegalArgumentException

const val START_ANIMATION_VALUE = 0f

const val DEFAULT_DURATION = 250L
const val DEFAULT_FADE_DURATION = 250L

fun <V : View> V.fadeIn(
    duration: Long = DEFAULT_FADE_DURATION,
    mAlpha: Float = 1f,
    onEnd: (Animator?) -> Unit = {}
) = anim(0f, mAlpha, duration, onStart = {
    alpha = 0f
    visibility = View.VISIBLE
}, onEnd = onEnd) {
    alpha = it.animatedValue as Float
}

fun <V : View> V.fadeOut(
    duration: Long = DEFAULT_FADE_DURATION,
    mAlpha: Float = 0f,
    onEnd: (Animator?) -> Unit = {}
) = anim(1f, mAlpha, duration, onStart = {
    visibility = View.VISIBLE
}, onEnd = onEnd) {
    alpha = it.animatedValue as Float
}

fun <V : View> V.fadeFromTo(
    from: Float,
    to: Float,
    duration: Long = DEFAULT_FADE_DURATION,
    onEnd: (Animator?) -> Unit = {}
) = anim(from, to, duration, onStart = {
    visibility = View.VISIBLE
}, onEnd = onEnd) {
    alpha = it.animatedValue as Float
}

fun anim(
    from: Number = START_ANIMATION_VALUE,
    to: Number = START_ANIMATION_VALUE,
    duration: Long = DEFAULT_DURATION,
    interpolator: Interpolator = LinearInterpolator(),
    repeatMode: Int? = null,
    repeatCount: Int? = null,
    delay: Long? = null,
    onStart: (Animator?) -> Unit = {},
    onEnd: (Animator?) -> Unit = {},
    onUpdateListener: (ValueAnimator) -> Unit = {}
) {
    val animator = when (to) {
        is Long, is Int -> ValueAnimator.ofInt(from.toInt(), to.toInt())
        is Float -> ValueAnimator.ofFloat(from.toFloat(), to)
        else -> throw IllegalArgumentException("Wrong number for Animation.")
    }

    animator.duration = duration
    animator.interpolator = interpolator

    repeatCount?.let { animator.repeatCount = it }
    repeatMode?.let { animator.repeatMode = it }

    delay?.let { animator.startDelay = it }

    animator.addUpdateListener {
        onUpdateListener(it)
    }

    animator.addListener(onEnd, onStart)

    animator.start()
}


