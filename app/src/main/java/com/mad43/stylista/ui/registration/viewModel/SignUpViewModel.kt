package com.mad43.stylista.ui.registration.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumerModel
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.Validation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SignUpViewModel (private val authUseCase : AuthUseCase = AuthUseCase()) : ViewModel()  {

    private var _signUpState: MutableStateFlow<RemoteStatus<SignupResponse>> = MutableStateFlow(RemoteStatus.Loading)
    var signUpState: StateFlow<RemoteStatus<SignupResponse>> = _signUpState

    private var _signUpStateLiveData: MutableLiveData<RemoteStatus<SignupResponse>> = MutableLiveData(RemoteStatus.Loading)
    var signUpStateLiveData: LiveData<RemoteStatus<SignupResponse>> = _signUpStateLiveData

    lateinit var userName : String
    lateinit var email : String
    lateinit var password : String
    lateinit var confirmPassword :String

    var isRegister : Boolean = false

    fun signUp(userName: String,email: String, password: String){
        viewModelScope.launch {
            try {
                val isSinupInFirbase = authUseCase.signUp(userName,email, password)
                Log.d(TAG, "////////signUp: ${isSinupInFirbase.toString()}")
                if (isSinupInFirbase != null){
                    val registerApi = authUseCase.registerUserInApi(userName,email,password)
                    val idCustumer = registerApi.body()?.customer?.id
                    if (idCustumer!=null){
                        authUseCase.getCardID(idCustumer)
                        authUseCase.getFavouriteID(idCustumer)
                        val favId =authUseCase.getFavouriteID(idCustumer).toString()
                        val idCard = authUseCase.getCardID(idCustumer).toString()
                        authUseCase.updateDataCustumer(idCustumer.toString(),
                            UpdateCustumer(UpdateCustumerModel(last_name = idCard, note = favId))
                        )
                    }
                    _signUpState.value = RemoteStatus.Success(registerApi.body()!!)
                    _signUpStateLiveData.value = RemoteStatus.Success(registerApi.body()!!)
                    authUseCase.sendEmailVerification()
                    isRegister = true

                }else{
                    _signUpStateLiveData.value = RemoteStatus.Valied(R.string.email_isExist)
                }
            }catch (e: FirebaseAuthUserCollisionException){
               // _signUpStateLiveData.value = RemoteStatus.Valied(R.string.email_isExist)
                Log.d(TAG, "Firbase signUp:::: ${e.message}")
                isRegister =false
            }
            catch (e : Exception){
                Log.d(TAG, "Firbase not stable signUp:::: ${e.message}")
            }
        }
    }

    fun validateInputs(userName: String ,email: String, password: String,confirmPassword : String) {
        if (userName.isEmpty()){

            //_signUpState.value = RemoteStatus.Valied(R.string.userNameEmpty)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.userNameEmpty)
            isRegister =false
        }
        else if (email.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.emailNameEmpty)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.emailNameEmpty)
            isRegister =false
        }else if(!email.matches(Validation.EMAIL_PATTERN.toRegex())){
            //_signUpState.value = RemoteStatus.Valied(R.string.validateEmail)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.validateEmail)
            isRegister =false
        }
        else if (password.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.passwordEmpty)
            isRegister =false
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.passwordEmpty)
        } else if (confirmPassword.isEmpty()) {
            _signUpState.value = RemoteStatus.Valied(R.string.confirmPasswordEmpty)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.confirmPasswordEmpty)
            isRegister =false
        }else if(password != confirmPassword){
            _signUpState.value = RemoteStatus.Valied(R.string.matchPassword)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.matchPassword)
            isRegister =false
        }
        else if (!password.matches(Validation.PASSWORD_PATTERN.toRegex())) {
            _signUpState.value = RemoteStatus.Valied(R.string.validatePassword)
            _signUpStateLiveData.value = RemoteStatus.Valied(R.string.validatePassword)
            isRegister =false
        }
        else {
            signUp(userName,email,password)

        }
    }

}