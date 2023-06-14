package com.mad43.stylista.ui.registration.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mad43.stylista.R
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.Validation
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel()  {
    private val validationMutableStateFlow = MutableStateFlow<SignUpState>(SignUpState.BeforeValidation)
    val validationStateFlow: StateFlow<SignUpState> = validationMutableStateFlow

    private val errorMStateFow = MutableSharedFlow<Throwable>()
    val errorStateFlow: SharedFlow<Throwable?> = errorMStateFow

    fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                authUseCase.signUp(email, password)
                validationMutableStateFlow.value = SignUpState.onSuccess(R.string.success)
                authUseCase.sendEmailVerification()
            }catch (e: FirebaseAuthUserCollisionException){
                validationMutableStateFlow.value = SignUpState.onError(R.string.email_isExist)
            }
            catch (e: Exception) {
                errorMStateFow.emit(e)
            }
        }
    }

    fun validateInputs(email: String, password: String,confirmPassword : String) {
        if (email.isEmpty()) {
            validationMutableStateFlow.value = SignUpState.onError(R.string.emailNameEmpty)
        }else if(!email.matches(Validation.EMAIL_PATTERN.toRegex())){
            validationMutableStateFlow.value = SignUpState.onError(R.string.validateEmail)
        }
        else if (password.isEmpty()) {
            validationMutableStateFlow.value = SignUpState.onError(R.string.passwordEmpty)
        } else if (confirmPassword.isEmpty()) {
            validationMutableStateFlow.value = SignUpState.onError(R.string.confirmPasswordEmpty)
        }else if(password != confirmPassword){
            validationMutableStateFlow.value = SignUpState.onError(R.string.matchPassword)
        }
        else if (!password.matches(Validation.PASSWORD_PATTERN.toRegex())) {
            validationMutableStateFlow.value = SignUpState.onError(R.string.validatePassword)
        }
        else {

            signUp(email,password)

        }
    }

}