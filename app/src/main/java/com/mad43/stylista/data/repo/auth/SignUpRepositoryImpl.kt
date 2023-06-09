package com.mad43.stylista.data.repo.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mad43.stylista.data.remote.entity.SignupModel
import com.mad43.stylista.data.remote.entity.SignupRequest
import com.mad43.stylista.data.remote.network.ApiService
import kotlinx.coroutines.tasks.await



class SignUpRepositoryImpl(
    private val auth: FirebaseAuth

) : SignUpRepository {

    override suspend fun signUp(email: String, password: String) {
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            auth.currentUser?.sendEmailVerification()?.await()
        } catch (e: FirebaseAuthUserCollisionException) {
            throw e
        } catch (e: Exception) {
            throw Exception("Failed to register user")
        }

        val userId = auth.currentUser?.uid
        registerUserInApi(userId, email, password)

    }

     override suspend fun registerUserInApi(userId: String?, email: String, password: String) {
        val signupRequest = SignupRequest(SignupModel(email,"","",password, note = userId!!))

        try {
            val response = ApiService.APIClient.signUpAPIService.signUpCustumer(signupRequest)
            if (!response.isSuccessful) {
                throw Exception("Failed to register user in API")
            }
        } catch (e: Exception) {
            throw Exception("Failed to register user in API")
        }
    }

    override suspend fun isEmailVerified(email: String): Boolean {
        val user = auth.fetchSignInMethodsForEmail(email).result
        return user == null || auth.currentUser?.isEmailVerified == true
    }

}