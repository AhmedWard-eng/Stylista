package com.mad43.stylista.data.remote.network.auth.retrofit

import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface LogInAPIInterface {
    @GET("customers.json")
    suspend fun LogInCustomers(@Query("email") email: String,
                               @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): Response<LoginResponse>
}

