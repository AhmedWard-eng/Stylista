package com.mad43.stylista.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.login.viewModel.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel() {

    private  var _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Loading)
    var loginState: StateFlow<LoginState> = _loginState

    fun getUserName():String{
        var userData = authUseCase.getCustomerData()
        var userName = userData.getOrNull()?.userName
        if (userName == null){
            return "guest"
        }else{
            return userName.toString()
        }
    }

    fun logout(){
        viewModelScope.launch {
            authUseCase.logout()
        }
    }
}