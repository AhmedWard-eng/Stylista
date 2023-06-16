package com.mad43.stylista.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(
        RemoteStatus.Loading)
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

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