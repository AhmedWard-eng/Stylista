package com.mad43.stylista.data.remote.dataSource.auth

import com.google.firebase.auth.FirebaseAuth
import com.mad43.stylista.data.remote.entity.SignupModel
import com.mad43.stylista.data.remote.entity.SignupRequest
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.ProductsAPIInterface
import com.mad43.stylista.data.remote.network.auth.retrofit.LogInAPIInterface
import com.mad43.stylista.data.remote.network.auth.retrofit.SignUpAPIInterface
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import retrofit2.Response

class AuthRemoteSourceImp (private val signupAPIInterface: SignUpAPIInterface=ApiService.APIClient.signUpAPIService ,private val loginAPIInterface: LogInAPIInterface = ApiService.APIClient.loginAPIService) : AuthRemoteSource {
    override suspend fun loginCustomer(email:String): Response<LoginResponse> {
        return loginAPIInterface.LogInCustomers(email)
    }

    override suspend fun registerUserInApi(userId: String?, email: String, password: String) {
        userId?.let { SignupModel(note = it,email=email,password=password) }
            ?.let { SignupRequest(it) }?.let { signupAPIInterface.signUpCustumer(it) }
    }

}