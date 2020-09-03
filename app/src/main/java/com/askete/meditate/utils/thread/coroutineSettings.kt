package com.askete.meditate.utils.thread

import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun LifecycleCoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit): Job =
    launch(Dispatchers.IO, block = block)

fun LifecycleOwner.main(block: suspend CoroutineScope.() -> Unit) : Job =
    lifecycleScope.launch(Dispatchers.Main,block = block)