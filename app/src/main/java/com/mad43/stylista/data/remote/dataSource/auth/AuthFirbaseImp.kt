package com.mad43.stylista.data.remote.dataSource.auth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.*
import com.mad43.stylista.data.remote.entity.auth.FirebaseCustumer
import kotlinx.coroutines.tasks.await

class AuthFirebaseImp(private val auth: FirebaseAuth = FirebaseAuth.getInstance()) : AuthFirbase {
    override fun getCurrentUser(): FirebaseCustumer? {
        return auth.currentUser?.let { FirebaseCustumer(it.uid, it.email) }
    }

    override fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun signOut() {
        auth.signOut()
    }

    override suspend fun signUp(email: String, password: String) : AuthResult? {
//        try {
//            auth.createUserWithEmailAndPassword(email, password).await()
//            return true
//        } catch (e: Exception) {
//            throw e
//            return false
//        }
    try {
        val authResult = FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        return authResult
    } catch (e: FirebaseAuthUserCollisionException) {
        return null
    }
}


    override suspend fun isEmailVerified(email: String): Boolean {
        return try {
            val user = auth.currentUser
            user != null && user.email == email && user.isEmailVerified
        } catch (e: Exception) {
            false
        }
    }
    override suspend fun sendEmailVerification() {
        try {
            auth.currentUser?.sendEmailVerification()?.await()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun signIn(email: String, password: String): FirebaseCustumer? {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            authResult.user?.let { FirebaseCustumer(it.uid, it.email) }
        } catch (e: Exception) {
            throw e
        }
    }


}