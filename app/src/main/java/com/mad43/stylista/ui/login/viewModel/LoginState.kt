package com.mad43.stylista.ui.login.viewModel

import com.mad43.stylista.data.remote.entity.auth.LoginResponse

sealed class LoginState {
    data class Success(val data: LoginResponse) : LoginState()
    data class Failed(val message: Int) : LoginState()
    object Loading : LoginState()
}