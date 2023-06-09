package com.mad43.stylista.data.sharedPreferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_PREF


object SharedPref {
    lateinit var context: Application
    lateinit var sharedPreferences: SharedPreferences

    fun init(context: Application) {
        this.context = context
        sharedPreferences = context.getSharedPreferences(CUSTOMER_PREF, Context.MODE_PRIVATE)
    }
}