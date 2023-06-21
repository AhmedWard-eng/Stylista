package com.mad43.stylista.data.remote.dataSource.auth

import com.mad43.stylista.data.remote.entity.auth.Customer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import kotlinx.coroutines.flow.Flow

import retrofit2.Response

interface AuthRemoteSource {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun registerUserInApi(userName: String, email: String, password: String): Response<SignupResponse>
    suspend fun updateDataCustumer(id : String, customer: UpdateCustumer): Response<Customer>

}