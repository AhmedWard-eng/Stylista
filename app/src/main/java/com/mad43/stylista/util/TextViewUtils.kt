package com.mad43.stylista.util

import android.widget.TextView
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import kotlinx.serialization.StringFormat

fun TextView.setPrice(price: Double) {
    val cManager = CurrencyManager()
    val pair = cManager.getCurrencyPair()
    val code = pair.first
    val rate = pair.second
    val newPrice = price * rate
    text = buildString {
        append(String.format("%.2f",newPrice))
        append(" $code")
    }
}