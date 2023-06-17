package com.mad43.stylista.ui.productInfo.viewModel

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductInfoViewModel (private val productInfo: ProductInfo = ProductInfo(), private val favourite : FavouriteLocal): ViewModel() {

    private val _uiState = MutableStateFlow<ApiState>(ApiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _uiStateNetwork = MutableStateFlow<RemoteStatus<CustomDraftOrderResponse>>(RemoteStatus.Loading)
    val uiStateNetwork: StateFlow<RemoteStatus<CustomDraftOrderResponse>> = _uiStateNetwork

    val imagesArray = ArrayList<SlideModel>()

    private val _favourite = MutableStateFlow<RemoteStatus<List<Favourite>>>(RemoteStatus.Loading)
    val favouriteList = _favourite.asStateFlow()

    private val _isfavourite = MutableStateFlow<RemoteStatus<Boolean>>(RemoteStatus.Loading)
    val isfavouriteExist = _isfavourite.asStateFlow()

    fun getProductDetails(id: Long){
        viewModelScope.launch (Dispatchers.IO){
            productInfo.getProductDetails(id).catch {   e->_uiState.value=ApiState.Failure(e) }
                .collect{
                        data ->
                    _uiState.value=ApiState.Success(data)
                    for (i in 0..data.product.images.size-1){
                        var imag = data.product.images.get(i).src
                        imagesArray.add(SlideModel(imag))
                    }

                }
        }
    }

    fun insertProduct(product: Favourite){
        viewModelScope.launch (Dispatchers.IO){
            favourite.insertProduct(product)
        }
    }
    fun deleteProduct(product: Favourite){
        viewModelScope.launch (Dispatchers.IO){
            favourite.deleteProduct(product)
        }
    }

    fun getLocalFavourite(){
        viewModelScope.launch (Dispatchers.IO){
            favourite.getStoredProduct()
                .catch {
                        e->_favourite.value=RemoteStatus.Failure(e)
                    Log.i(ContentValues.TAG, "getLocalFavourite: FailureFailureFailureFailure")
                }
                .collect{
                        data ->
                    _favourite.value=RemoteStatus.Success(data)
                }
        }
    }

    fun isFavourite(productID : Long){
        viewModelScope.launch {
            favourite.isProductFavorite(productID).catch {
                    e->_isfavourite.value=RemoteStatus.Failure(e)
            }.collectLatest {
                data ->
                _isfavourite.value= RemoteStatus.Success(data)
            }
        }
    }

    fun insertFavouriteForCustumer(id : Long, draftOrderPutBody: DraftOrderPutBody)  {
        viewModelScope.launch (Dispatchers.IO){
             favourite.insertFavouriteForCustumer(id = id, draftOrderPutBody)
        }

    }

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

    fun getFavouriteUsingId(idFavourite : String){
        viewModelScope.launch {
            try {
                val customDraftOrderResponse = favourite.getFavouriteUsingId(idFavourite)
                _uiStateNetwork.value = customDraftOrderResponse
                Log.d(TAG, "getFavouriteUsingId: ${_uiStateNetwork.value}")
            } catch (e: Exception) {
                _uiStateNetwork.value = RemoteStatus.Failure(e)
            }
        }
    }

}

