package com.mad43.stylista.data.repo.auth

interface SignUpRepository {

    suspend fun signUp(email: String, password: String)
    suspend fun registerUserInApi(userId: String?, email: String, password: String)
    suspend fun isEmailVerified(email: String): Boolean

}