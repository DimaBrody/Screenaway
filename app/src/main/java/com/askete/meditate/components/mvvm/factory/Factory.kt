package com.askete.meditate.components.mvvm.factory

interface Factory {
    fun <F> create(modelClass: Class<F>) : F
}