package com.mad43.stylista.data.sharedPreferences

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
private const val CURRENCY_PREF = "CURRENCY_PREF"
object CurrencySharedPref {
    lateinit var context: Application
    val sharedPreferences: SharedPreferences
        get() = context.getSharedPreferences(CURRENCY_PREF, Context.MODE_PRIVATE)
}