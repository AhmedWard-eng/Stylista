package com.mad43.stylista.data.repo.auth

import com.google.firebase.auth.AuthResult
import com.mad43.stylista.data.remote.entity.auth.*
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface AuthRepository {
    suspend fun loginCustomer(email:String): Response<LoginResponse>
    suspend fun registerUserInApi(userName: String, email: String, password: String) : Response<SignupResponse>
    suspend fun signUp(userName: String,email: String, password: String) : AuthResult?
    suspend fun isEmailVerified(email: String): Boolean
    fun getCurrentUser(): FirebaseCustumer?
    suspend fun saveLoggedInData(localCustomer : LocalCustomer)
    suspend fun logout()
    fun getCustomerData() : Result<LocalCustomer>
    suspend fun sendEmailVerification()
    suspend fun signIn(email: String, password: String): FirebaseCustumer?
    fun isUserLoggedIn(): Boolean
    suspend fun updateDataCustumer(id: String, customer: UpdateCustumer): Response<Customer>
}