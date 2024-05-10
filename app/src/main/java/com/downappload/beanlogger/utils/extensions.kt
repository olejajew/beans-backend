package com.downappload.beanlogger.utils

import android.view.View

fun View.hide(){
    visibility = View.GONE
}

fun View.show(alpha: Float = 1.0f){
    visibility = View.VISIBLE
    setAlpha(alpha)
}