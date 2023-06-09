package com.mad43.stylista.ui.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.data.repo.auth.SignUpRepository
import com.mad43.stylista.domain.remote.auth.LoginUseCase


class LoginViewModelFactory(private val loginUseCase: LoginUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            LoginViewModel(loginUseCase) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}