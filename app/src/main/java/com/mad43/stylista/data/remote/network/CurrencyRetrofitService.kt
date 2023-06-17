package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.network.currency.CurrencyApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://api.apilayer.com/exchangerates_data/"
object CurrencyRetrofitService {

    private val client = OkHttpClient
        .Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    private val retrofit: Retrofit =  Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val currencyApiInterface: CurrencyApiInterface = retrofit.create(CurrencyApiInterface::class.java)


}