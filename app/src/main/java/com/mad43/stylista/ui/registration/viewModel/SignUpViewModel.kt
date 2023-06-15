package com.mad43.stylista.ui.registration.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel()  {

    private var _signUpState: MutableStateFlow<RemoteStatus<SignupResponse>> = MutableStateFlow(RemoteStatus.Loading)
    var signUpState: StateFlow<RemoteStatus<SignupResponse>> = _signUpState

    fun signUp(userName: String,email: String, password: String){
        viewModelScope.launch {
            try {
                var isSinupInFirbase = authUseCase.signUp(userName,email, password)
                if (isSinupInFirbase){
                    var registerApi = authUseCase.registerUserInApi(userName,email,password)
                    var idCustumer = registerApi.body()?.customer?.id
                    if (idCustumer!=null){
                        authUseCase.getCardID(idCustumer)
                        authUseCase.getFavouriteID(idCustumer)
                    }
                    _signUpState.value = RemoteStatus.Success(registerApi.body()!!)
                    authUseCase.sendEmailVerification()

                }
            }catch (e: FirebaseAuthUserCollisionException){
                _signUpState.value = RemoteStatus.Valied(R.string.email_isExist)
                Log.d(TAG, "Firbase signUp:::: ${e.message}")
            }
        }
    }

    fun validateInputs(userName: String ,email: String, password: String,confirmPassword : String) {
        if (userName.isEmpty()){
            _signUpState.value = RemoteStatus.Valied(R.string.userNameEmpty)
        }
        else if (email.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.emailNameEmpty)
        }else if(!email.matches(Validation.EMAIL_PATTERN.toRegex())){
            _signUpState.value = RemoteStatus.Valied(R.string.validateEmail)
        }
        else if (password.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.passwordEmpty)
        } else if (confirmPassword.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.confirmPasswordEmpty)
        }else if(password != confirmPassword){
            _signUpState.value = RemoteStatus.Valied(R.string.matchPassword)
        }
        else if (!password.matches(Validation.PASSWORD_PATTERN.toRegex())) {
            _signUpState.value = RemoteStatus.Valied(R.string.validatePassword)
        }
        else {

            signUp(userName,email,password)

        }
    }

}