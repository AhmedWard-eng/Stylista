package com.mad43.stylista.data.remote.dataSource.auth

import com.mad43.stylista.data.remote.entity.SignupModel
import com.mad43.stylista.data.remote.entity.SignupRequest
import com.mad43.stylista.data.remote.entity.auth.Customer
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.auth.AuthAPIInterface
import retrofit2.Response

class AuthRemoteSourceImp (private val authAPIInterface: AuthAPIInterface = ApiService.authApiService) : AuthRemoteSource {
    override suspend fun loginCustomer(email:String): Response<LoginResponse> {
        return authAPIInterface.LogInCustomers(email)
    }

    override suspend fun registerUserInApi(userName: String, email: String, password: String) {
        authAPIInterface.signUpCustumer(SignupRequest(SignupModel(first_name = userName, email = email, password = password)))
    }

    override suspend fun updateDataCustumer(id: Long, customer: UpdateCustumer): Response<Customer> {
       return authAPIInterface.updateDataCustumer(id, customer)
    }




}