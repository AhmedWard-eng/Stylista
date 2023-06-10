package com.mad43.stylista.data.repo.auth

import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.network.auth.retrofit.LogInAPIInterface
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import retrofit2.Response

class LoginRepositoryImp(private val logInApi: LogInAPIInterface,private val userManager: CustomerManager = PreferencesData()
) :LoginRepository{

    override suspend fun loginCustomer(email:String): Response<LoginResponse> {
        return logInApi.LogInCustomers(email)
    }

    override suspend fun saveLoggedInData(localCustomer: LocalCustomer) {
        userManager.saveCustomerData(localCustomer)
    }
    override suspend fun logout() {
        userManager.removeCustomerData()
    }

    override fun getCustomerData(): Result<LocalCustomer> {
       return userManager.getCustomerData()
    }


}