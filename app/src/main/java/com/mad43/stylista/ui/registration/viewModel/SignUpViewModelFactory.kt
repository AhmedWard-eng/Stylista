package com.mad43.stylista.ui.registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.domain.remote.auth.SignUpUseCase

class SignUpViewModelFactory (private val signUpUseCase: SignUpUseCase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignUpViewModel::class.java)){
            SignUpViewModel(signUpUseCase) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}