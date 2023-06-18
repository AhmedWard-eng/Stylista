package com.mad43.stylista.ui.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel

class LoginViewModelFactory (private val authUseCase : AuthUseCase = AuthUseCase(),val favourite : FavouriteLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(LoginViewModel::class.java)){
            LoginViewModel(authUseCase,favourite) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}
