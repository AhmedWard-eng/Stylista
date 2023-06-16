package com.mad43.stylista.ui.login.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumerModel
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.domain.remote.cart.CreateCartUseCase
import com.mad43.stylista.domain.remote.favourite.CreateFavouriteUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(
        RemoteStatus.Loading)
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

    var checkLogin = authUseCase.isUserLoggedIn()


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authUseCase.loginCustomer(email)

                if (user.isSuccessful){
                    val data = user.body()
                    if (data != null) {

                        if(data.customers[0].tags == password){

                            authUseCase.signIn(email,password)
                            var checkEmail = authUseCase.isEmailVerified(email)
                           if (checkEmail){
                               _loginState.value = RemoteStatus.Success(data)
                               authUseCase.saveLoggedInData(LocalCustomer(customerId = data.customers[0].id,email= email,
                                   state = true, userName = data.customers[0].first_name, cardID = data.customers[0].last_name,
                                   favouriteID = data.customers[0].note))

                           }else{
                               _loginState.value = RemoteStatus.Valied(R.string.verfid)
                           }

                        }else{
                            _loginState.value = RemoteStatus.Valied(R.string.login_valid_password)
                        }
                    }else {
                        _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                    }
                }else{
                    _loginState.value = RemoteStatus.Valied(R.string.login_faild)
                }

            } catch (e: Exception) {
                _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                Log.d(TAG, "Exception:  ${e.message}")
            }
        }
    }

    fun checkUserIsLogin(){
        val customerData = authUseCase.getCustomerData()
        _userExists.value = customerData.isSuccess
    }


}