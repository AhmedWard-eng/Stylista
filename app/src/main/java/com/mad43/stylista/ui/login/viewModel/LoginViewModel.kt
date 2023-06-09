package com.mad43.stylista.ui.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.integrity.e
import com.mad43.stylista.R
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.remote.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private  var _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Loading)
    var loginState: StateFlow<LoginState> = _loginState

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()



    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = loginUseCase.loginCustomer(email)

                if (user.isSuccessful){
                    val data = user.body()
                    if (data != null) {
                        if(data.customers[0].tags == password){
                            _loginState.value = LoginState.Success(data)
                            loginUseCase.saveLoggedInData(LocalCustomer(data.customers[0].id,email,true,data.customers[0].note))
                        }else{
                            _loginState.value = LoginState.Failed(R.string.login_valid_password)
                        }
                    }else {
                        _loginState.value = LoginState.Failed(R.string.login_valid_email)
                    }
                }else{
                    _loginState.value = LoginState.Failed(R.string.login_faild)
                }
            } catch (e: Exception) {
                _loginState.value = LoginState.Failed(R.string.login_valid_email)
            }
        }
    }

    fun checkUserIsLogin(){
        val customerData = loginUseCase.getCustomerData()
        _userExists.value = customerData.isSuccess
    }
}