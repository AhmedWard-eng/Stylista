package com.mad43.stylista.domain.remote.auth

import com.mad43.stylista.data.remote.entity.auth.Customer
import com.mad43.stylista.data.remote.entity.auth.FirebaseCustumer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.data.repo.auth.AuthRepository
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import retrofit2.Response


class AuthUseCase(val authRepository: AuthRepository = AuthRepositoryImp()) {
    suspend fun signUp(userName: String,email: String, password: String) {
        authRepository.signUp(userName,email, password)
    }
    suspend fun loginCustomer(email:String): Response<LoginResponse> {
        return   authRepository.loginCustomer(email)
    }

    suspend fun isEmailVerified(email: String): Boolean{
        return authRepository.isEmailVerified(email)
    }
    suspend fun saveLoggedInData(localCustomer : LocalCustomer){
        authRepository.saveLoggedInData(localCustomer)
    }
    suspend fun logout(){
        authRepository.logout()
    }
    fun getCustomerData() : Result<LocalCustomer>{
        return authRepository.getCustomerData()
    }
    suspend fun sendEmailVerification(){
        authRepository.sendEmailVerification()
    }
    suspend fun signIn(email: String, password: String): FirebaseCustumer?{
        return authRepository.signIn(email,password)
    }
    fun isUserLoggedIn(): Boolean{
        return authRepository.isUserLoggedIn()
    }

    suspend fun updateDataCustumer(id: Long, custumer: UpdateCustumer): Response<Customer>{
       return authRepository.updateDataCustumer(id,custumer)
    }
}