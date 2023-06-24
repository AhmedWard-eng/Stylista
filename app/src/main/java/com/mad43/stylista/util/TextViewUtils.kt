package com.mad43.stylista.util

import android.annotation.SuppressLint
import android.widget.TextView
import com.mad43.stylista.data.sharedPreferences.currency.CurrencyManager
import kotlinx.serialization.StringFormat
import java.text.SimpleDateFormat
import java.util.Locale

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

@SuppressLint("SimpleDateFormat")
fun TextView.formatDate(date : String){
    date.let {
        if (it.isBlank()){
            text = buildString {
                append(it)
            }
        }

        val dateTimeGlobalPattern  = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val dayPattern = "dd/MM/yyyy"
        val format = SimpleDateFormat(dateTimeGlobalPattern , Locale.US)
        val myDate = format.parse(it)
        text = buildString {
            append(SimpleDateFormat(dayPattern).format(myDate))
        }

    }
}

fun TextView.title(titleProduct : String){
    val titleList = titleProduct.split("|")

    val title = if (titleList.size == 1) {
        titleList[0]
    }else {
        titleList[1]
    }

    text = buildString {
        append(title)
    }

}