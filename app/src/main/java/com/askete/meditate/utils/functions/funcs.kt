package com.askete.meditate.utils.functions

import java.lang.Exception

fun simpleTry(onTry: () -> Unit) = try {
    onTry()
} catch (e: Exception){
    e.printStackTrace()
}