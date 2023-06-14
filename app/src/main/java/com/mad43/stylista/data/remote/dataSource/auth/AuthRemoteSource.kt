package com.mad43.stylista.data.remote.dataSource.auth

import com.mad43.stylista.data.remote.entity.auth.LoginResponse

import retrofit2.Response

interface AuthRemoteSource {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun registerUserInApi(userId: String?, email: String, password: String)

}