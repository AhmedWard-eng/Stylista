package com.mad43.stylista.domain.remote.auth

import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.repo.auth.LoginRepository
import com.mad43.stylista.data.repo.auth.SignUpRepository
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import retrofit2.Response

class LoginUseCase(val loginRepo:LoginRepository , val signUpRepository: SignUpRepository) {

    suspend fun loginCustomer(email:String): Response<LoginResponse> {
          return   loginRepo.loginCustomer(email)
    }

    suspend fun isEmailVerified(email: String): Boolean{
        return signUpRepository.isEmailVerified(email)
    }
    suspend fun saveLoggedInData(localCustomer : LocalCustomer){
        loginRepo.saveLoggedInData(localCustomer)
    }
    suspend fun logout(){
        loginRepo.logout()
    }
    fun getCustomerData() : Result<LocalCustomer>{
        return loginRepo.getCustomerData()
    }
}