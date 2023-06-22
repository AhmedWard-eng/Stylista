package com.mad43.stylista.domain.remote.auth

import com.google.firebase.auth.AuthResult
import com.mad43.stylista.data.remote.entity.auth.*
import com.mad43.stylista.data.repo.auth.AuthRepository
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.cart.CreateCartUseCase
import com.mad43.stylista.domain.remote.favourite.CreateFavouriteUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import kotlin.math.log
import kotlin.time.Duration.Companion.days


class AuthUseCase(val authRepository: AuthRepository = AuthRepositoryImp()) {
    suspend fun signUp(userName: String,email: String, password: String) : AuthResult? {
     return authRepository.signUp(userName,email,password)
    }
    suspend fun loginCustomer(email:String): Response<LoginResponse> {
        return authRepository.loginCustomer(email)
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

    suspend fun updateDataCustumer(id: String, custumer: UpdateCustumer): Response<Customer>{
       return authRepository.updateDataCustumer(id,custumer)
    }
    suspend fun registerUserInApi(userName: String, email: String, password: String) : Response<SignupResponse>{
        return authRepository.registerUserInApi(userName,email,password)
    }
    suspend fun getCardID(customerId : Long):Long{
        var cardID = CreateCartUseCase()
        val remoteStatus = cardID(customerId)
        return when (remoteStatus) {
            is RemoteStatus.Success -> remoteStatus.data.toLong()
            else -> 0
        }
    }
    suspend fun getFavouriteID(customerId: Long):Long{
        var favID = CreateFavouriteUseCase()
        val remoteStatus = favID(customerId)
        return when (remoteStatus) {
            is RemoteStatus.Success -> remoteStatus.data.toLong()
            else -> 0
        }
    }
}