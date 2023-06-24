package com.mad43.stylista.data.remote.dataSource.auth


import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mad43.stylista.data.remote.entity.auth.FirebaseCustumer

interface AuthFirbase {
    fun getCurrentUser(): FirebaseCustumer?
    fun isUserLoggedIn(): Boolean
    fun signOut()
    suspend fun signUp(email: String, password: String) : AuthResult?
    suspend fun isEmailVerified(email: String): Boolean
    suspend fun sendEmailVerification()
    suspend fun signIn(email: String, password: String): FirebaseCustumer?

}