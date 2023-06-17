package com.mad43.stylista.data.remote.entity.currency

data class CurrencyRate(
    val base: String?,
    val date: String?,
    val rates: Map<String,Double>,
    val success: Boolean?,
    val timestamp: Int?
)