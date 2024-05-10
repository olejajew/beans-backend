package com.downappload.beanlogger

import android.app.Application
import android.widget.Toast

class AppClass : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    companion object{

        private lateinit var instance: AppClass

        fun getInstance() = instance

    }

}