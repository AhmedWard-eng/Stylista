package com.mad43.stylista.data.remote.network.currency

import com.mad43.stylista.BuildConfig
import com.mad43.stylista.data.remote.entity.currency.CurrencyRate
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApiInterface {
    @GET("latest")
    suspend fun getCurrenciesRate(@Query("base") base : String = "EGP",@Query("apikey") apiKey : String = BuildConfig.CURRENCY_API_KEY) : CurrencyRate
}