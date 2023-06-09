package com.mad43.stylista.domain.remote.auth

import com.mad43.stylista.data.repo.auth.SignUpRepository

class SignUpUseCase (private val authRepository: SignUpRepository) {
    suspend fun signUp(email: String, password: String) {
         authRepository.signUp(email, password)
    }
    suspend fun signUpAPI(userId: String?, email: String, password: String){
        authRepository.registerUserInApi(userId, email, password)
    }
    suspend fun isEmailVerified(email: String): Boolean{
       return authRepository.isEmailVerified(email)
    }



}