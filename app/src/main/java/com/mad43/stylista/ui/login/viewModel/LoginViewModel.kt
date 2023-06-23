package com.mad43.stylista.ui.login.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mad43.stylista.R
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.DraftOrder
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel (private val authUseCase : AuthUseCase = AuthUseCase(),val favourite : FavouriteLocal) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(RemoteStatus.Loading)
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

    private var draftOrderList : DraftOrder ?= null

    private val _draftOrder = MutableStateFlow<DraftOrderState>(DraftOrderState.Loading)
    val draftOrder: StateFlow<DraftOrderState> = _draftOrder

    fun getIDForFavourite(): Long {
        val customerData = favourite.getIDFavouriteForCustumer()

        return if (customerData.isSuccess) {
            val localCustomer = customerData.getOrNull()
            val favouriteId = localCustomer?.favouriteID
            if (favouriteId != null) {
                favouriteId.toLong()
            } else {
                throw Exception("Favourite ID not found")
            }
        } else {
            throw Exception("Customer data not found")
        }
    }
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authUseCase.loginCustomer(email)

                if (user.isSuccessful){
                    val data = user.body()
                    if (data != null) {

                        if(data.customers[0].tags == password){

                            authUseCase.signIn(email,password)
                            var checkEmail = authUseCase.isEmailVerified(email)
                           if (checkEmail){

                               _loginState.value = RemoteStatus.Success(data)
                               authUseCase.saveLoggedInData(LocalCustomer(customerId = data.customers[0].id,email= email,
                                   state = true, userName = data.customers[0].first_name, cardID = data.customers[0].last_name,
                                   favouriteID = data.customers[0].note))

                           }else{
                               _loginState.value = RemoteStatus.Valied(R.string.verfid)
                           }
                        }
                        else if(data.customers[0].tags != password){
                            _loginState.value = RemoteStatus.Valied(R.string.login_valid_password)
                            Log.d(TAG, "/////////////////////login password not match...: ")
                        }
                    }
                }else{
                    _loginState.value = RemoteStatus.Valied(R.string.login_faild)
                    Log.d(TAG, "///////ERRRRRRROOOOORRRRR login: ${user.errorBody()}")
                }

            } catch (e: IndexOutOfBoundsException) {
                _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                Log.d(TAG, "/////// API Exception:  ${e.message}.. ${e.localizedMessage},, $e")
            }
            catch (e: FirebaseAuthInvalidCredentialsException){
                _loginState.value = RemoteStatus.Valied(R.string.login_valid_password)
                Log.d(TAG, "///////Firbase Exception:  ${e.message}.. ${e.localizedMessage},, $e")
            }
            catch (e : FirebaseTooManyRequestsException){
                _loginState.value = RemoteStatus.Valied(R.string.firbase_signin_try_agin)
                Log.d(TAG, "///////FirebaseTooManyRequestsException Exception:  ${e.message}.. ${e.localizedMessage},, $e")
            }
        }
    }

    fun checkUserIsLogin(){
        val customerData = authUseCase.getCustomerData()
        _userExists.value = customerData.isSuccess
    }

    fun returnReload(){
        _loginState.value = RemoteStatus.Loading
    }

    fun getDraftOrder(idFav : String){
         viewModelScope.launch(Dispatchers.IO) {
            val remote = favourite.getFavouriteUsingId(idFav)
            when(remote){
                is RemoteStatus.Success ->{
                    val data = remote.data
                    draftOrderList = data.draft_order
                    _draftOrder.value = draftOrderList?.let { DraftOrderState.OnSuccess(it) }!!

                    Log.e("7777", "getDraftOrder: ${remote.data.draft_order}", )
                }
                is RemoteStatus.Failure -> {
                    _draftOrder.value = DraftOrderState.OnFail(remote.msg)
                    Log.e("7777", "FFFF: ${remote.msg.localizedMessage}", )
                }
                else ->{

                }
            }

        }

    }
    fun insertDraftOrder(product : Favourite){
        viewModelScope.launch(Dispatchers.IO) {
            favourite.insertProduct(product)
        }

    }


}