package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.entity.LoginResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LogInAPIInterface {
    @GET("customers/search.json")
    suspend fun LogInCustomers(@Query("email") email: String,
                               @Query("tag") password: String,
                               @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): Response<LoginResponse>
}