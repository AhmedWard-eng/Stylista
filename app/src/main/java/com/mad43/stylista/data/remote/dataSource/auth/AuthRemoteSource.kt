package com.mad43.stylista.data.remote.dataSource.auth

import com.mad43.stylista.data.remote.entity.auth.Customer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer

import retrofit2.Response

interface AuthRemoteSource {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun registerUserInApi(userName: String, email: String, password: String)
    suspend fun updateDataCustumer(id : Long, customer: UpdateCustumer): Response<Customer>

}