package com.mad43.stylista.ui.login.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.R
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.auth.LoginResponse
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumer
import com.mad43.stylista.data.remote.entity.auth.UpdateCustumerModel
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.domain.remote.cart.CreateCartUseCase
import com.mad43.stylista.domain.remote.favourite.CreateFavouriteUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel (private val authUseCase : AuthUseCase = AuthUseCase(),val favourite : FavouriteLocal) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(RemoteStatus.Loading)
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

    var checkLogin = authUseCase.isUserLoggedIn()



    private fun getIDForFavourite(): String {
        var idFavourite = getIDForFavourite()
        return idFavourite
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
                               Log.d(TAG, "///////////login: //////////////////////////////////")

                           }else{
                               _loginState.value = RemoteStatus.Valied(R.string.verfid)
                           }

                        }else{
                            _loginState.value = RemoteStatus.Valied(R.string.login_valid_password)
                        }
                    }else {
                        _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                    }
                }else{
                    _loginState.value = RemoteStatus.Valied(R.string.login_faild)
                }

            } catch (e: Exception) {
                _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                Log.d(TAG, "Exception:  ${e.message}")
            }
        }
    }

    fun checkUserIsLogin(){
        val customerData = authUseCase.getCustomerData()
        _userExists.value = customerData.isSuccess
    }

    suspend fun getLineItems(idFav: String): MutableList<CustomDraftOrderResponse>{
        var getProduct = favourite.getFavouriteUsingId(idFav)

        return when (getProduct) {
            is RemoteStatus.Success -> mutableListOf(getProduct.data)
            else -> mutableListOf()
        }
    }

    suspend fun insertAllProductDB(){
        var faviuriteID = getIDForFavourite()
        var draftOrder = getLineItems(idFav = faviuriteID )
        for (response in draftOrder) {
            val draftOrder = response.draft_order
            if (draftOrder != null && draftOrder.line_items != null) {
                for (lineItem in draftOrder.line_items) {
                    var properties = lineItem.properties
                    val urlImage = properties?.find { it.name == "url_image" }?.value
                    if (lineItem.title!= null && lineItem.price!=null && lineItem.product_id!=null&& urlImage!=null && lineItem.variant_id!=null){
                        var favouriteProduct = Favourite(lineItem.product_id,lineItem.title,lineItem.price, image = urlImage,lineItem.variant_id)
                        viewModelScope.launch(Dispatchers.IO){
                            favourite.insertProduct(favouriteProduct)
                        }

                    }
                }
            }
        }
    }

}