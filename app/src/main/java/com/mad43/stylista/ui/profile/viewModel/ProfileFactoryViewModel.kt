package com.mad43.stylista.ui.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase


class ProfileFactoryViewModel (private val authUseCase : AuthUseCase = AuthUseCase(), val favourite : FavouriteLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
            ProfileViewModel(authUseCase,favourite) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}