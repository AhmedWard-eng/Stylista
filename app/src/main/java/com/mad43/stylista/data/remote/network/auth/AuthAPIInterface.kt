package com.mad43.stylista.data.remote.network.auth

import com.mad43.stylista.data.remote.entity.SignupRequest
import com.mad43.stylista.data.remote.entity.auth.Customer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.*

interface AuthAPIInterface {

    @POST("customers.json")
    suspend fun signUpCustumer(
        @Body body: SignupRequest,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
    ): Response<SignupResponse>

    @GET("customers.json")
    suspend fun LogInCustomers(@Query("email") email: String,
                               @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): Response<LoginResponse>

    @PUT("customers/{id}.json")
    suspend fun updateDataCustumer(
        @Path("id") id: String,
        @Body customer: UpdateCustumer,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
    ): Response<Customer>



}