package com.mad43.stylista.data.repo.auth

import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import retrofit2.Response

interface LoginRepository {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun saveLoggedInData(localCustomer : LocalCustomer)
    suspend fun logout()
    fun getCustomerData() : Result<LocalCustomer>
}

