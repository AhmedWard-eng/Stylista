package com.mad43.stylista.data.repo.auth

import com.mad43.stylista.data.remote.dataSource.auth.AuthFirbase
import com.mad43.stylista.data.remote.dataSource.auth.AuthFirebaseImp
import com.mad43.stylista.data.remote.dataSource.auth.AuthRemoteSource
import com.mad43.stylista.data.remote.dataSource.auth.AuthRemoteSourceImp
import com.mad43.stylista.data.remote.entity.auth.*
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class AuthRepositoryImp(private val authRemoteSource: AuthRemoteSource = AuthRemoteSourceImp(),private  val authFirbase: AuthFirbase=AuthFirebaseImp(),private val userManager: CustomerManager = PreferencesData()):AuthRepository {
    override suspend fun loginCustomer(email: String): Response<LoginResponse> {
        return authRemoteSource.loginCustomer(email)
    }

    override suspend fun registerUserInApi(userName: String, email: String, password: String) : Response<SignupResponse> {
        return authRemoteSource.registerUserInApi(userName,email,password)
    }

    override suspend fun signUp(userName: String,email: String, password: String) : Boolean {
        return authFirbase.signUp(email,password)
    }

    override suspend fun isEmailVerified(email: String): Boolean {
        return authFirbase.isEmailVerified(email)
    }

    override fun getCurrentUser(): FirebaseCustumer? {
       return authFirbase.getCurrentUser()
    }
    override suspend fun saveLoggedInData(localCustomer: LocalCustomer) {
        userManager.saveCustomerData(localCustomer)
    }
    override suspend fun logout() {
        userManager.removeCustomerData()
        authFirbase.signOut()
    }

    override fun getCustomerData(): Result<LocalCustomer> {
        return userManager.getCustomerData()
    }

    override suspend fun sendEmailVerification() {
        authFirbase.sendEmailVerification()
    }

    override suspend fun signIn(email: String, password: String): FirebaseCustumer? {
        return authFirbase.signIn(email,password)
    }
    override fun isUserLoggedIn(): Boolean{
        return authFirbase.isUserLoggedIn()
    }

    override suspend fun updateDataCustumer(id: String, customer: UpdateCustumer): Response<Customer> {
        return authRemoteSource.updateDataCustumer(id, customer)
    }


}