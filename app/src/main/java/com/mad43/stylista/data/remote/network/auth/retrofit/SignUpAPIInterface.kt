package com.mad43.stylista.data.remote.network.auth.retrofit

import com.mad43.stylista.data.remote.entity.*
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.*
import java.util.*

interface SignUpAPIInterface {

    @POST("customers.json")
    suspend fun signUpCustumer(
        @Body body: SignupRequest,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
    ): Response<SignupResponse>


}