package com.mad43.stylista.ui.login.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel (private val authUseCase : AuthUseCase = AuthUseCase(),val favourite : FavouriteLocal) : ViewModel() {

    private var _loginState: MutableStateFlow<RemoteStatus<LoginResponse>> = MutableStateFlow(RemoteStatus.Loading)
    var loginState: StateFlow<RemoteStatus<LoginResponse>> = _loginState

    private val _userExists = MutableStateFlow(false)
    val userExists: StateFlow<Boolean> = _userExists.asStateFlow()

    private var _signInStateLiveData: MutableLiveData<RemoteStatus<LoginResponse>>  = MutableLiveData(RemoteStatus.Loading)
    var signInStateLiveData: LiveData<RemoteStatus<LoginResponse>>  = _signInStateLiveData

    var checkLogin = authUseCase.isUserLoggedIn()



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
                               _signInStateLiveData.value = RemoteStatus.Success(data)
                               authUseCase.saveLoggedInData(LocalCustomer(customerId = data.customers[0].id,email= email,
                                   state = true, userName = data.customers[0].first_name, cardID = data.customers[0].last_name,
                                   favouriteID = data.customers[0].note))

                           }else{
                               _loginState.value = RemoteStatus.Valied(R.string.verfid)
                               _signInStateLiveData.value = RemoteStatus.Valied(R.string.verfid)
                           }

                        }else{
                            _loginState.value = RemoteStatus.Valied(R.string.login_valid_password)
                            _signInStateLiveData.value = RemoteStatus.Valied(R.string.login_valid_password)
                        }
                    }else {
                        _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                        _signInStateLiveData.value = RemoteStatus.Valied(R.string.login_valid_email)
                        Log.d(TAG, "login: NULLLLLLLLLLLLLLLLLLLLLLLLLLL")
                    }
                }else{
                    _loginState.value = RemoteStatus.Valied(R.string.login_faild)
                    _signInStateLiveData.value = RemoteStatus.Valied(R.string.login_faild)
                    Log.d(TAG, "ERRRRRRROOOOORRRRR login: ${user.errorBody()}")
                }

            } catch (e: Exception) {
                _loginState.value = RemoteStatus.Valied(R.string.login_valid_email)
                _signInStateLiveData.value = RemoteStatus.Valied(R.string.login_valid_email)
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
        var draftOrder = getLineItems(idFav = faviuriteID.toString() )
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

    fun insertAll(){
        viewModelScope.launch {
            insertAllProductDB()
        }
    }

}