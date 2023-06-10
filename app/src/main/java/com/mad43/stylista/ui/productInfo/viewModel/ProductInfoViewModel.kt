package com.mad43.stylista.ui.productInfo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.domain.remote.productInfo.ProductInfo
import com.mad43.stylista.ui.productInfo.model.ApiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProductInfoViewModel (private val productInfo: ProductInfo = ProductInfo()): ViewModel() {

    private val _uiState = MutableStateFlow<ApiState>(ApiState.Loading)
    val uiState = _uiState.asStateFlow()
    val imagesArray = ArrayList<SlideModel>()
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


}