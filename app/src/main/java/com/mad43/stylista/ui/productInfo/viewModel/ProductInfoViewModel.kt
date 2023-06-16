package com.mad43.stylista.ui.productInfo.viewModel

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductInfoViewModel (private val productInfo: ProductInfo = ProductInfo(), private val favourite : FavouriteLocal): ViewModel() {

    private val _uiState = MutableStateFlow<ApiState>(ApiState.Loading)
    val uiState = _uiState.asStateFlow()
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

}

