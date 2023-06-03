package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.entity.*
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import java.util.*

interface SignUpAPIInterface {

    @POST("customers.json")
    suspend fun signUpCustumer(
        @Body body: SignupRequest,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
    ): Response<SignupResponse>

}