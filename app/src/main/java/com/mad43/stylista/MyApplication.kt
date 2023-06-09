package com.mad43.stylista

import android.app.Application
import com.mad43.stylista.data.sharedPreferences.SharedPref

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPref.init(this)
    }
}