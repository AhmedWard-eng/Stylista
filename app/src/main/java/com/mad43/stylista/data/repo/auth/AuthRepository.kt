package com.mad43.stylista.data.repo.auth

import com.mad43.stylista.data.remote.entity.auth.FirebaseCustumer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import retrofit2.Response

interface AuthRepository {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun registerUserInApi(userId: String?, email: String, password: String)
    suspend fun signUp(email: String, password: String)
    suspend fun isEmailVerified(email: String): Boolean
    fun getCurrentUser(): FirebaseCustumer?

    suspend fun saveLoggedInData(localCustomer : LocalCustomer)
    suspend fun logout()
    fun getCustomerData() : Result<LocalCustomer>
    suspend fun sendEmailVerification()
    suspend fun signIn(email: String, password: String): FirebaseCustumer?
    fun isUserLoggedIn(): Boolean
}