package com.mad43.stylista.util

import android.widget.TextView

fun TextView.setPrice(price: Double) {
    text = buildString {
        append(price.toString())
        append(" EGP")
    }
}