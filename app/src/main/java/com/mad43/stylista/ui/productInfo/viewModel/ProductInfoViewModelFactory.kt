package com.mad43.stylista.ui.productInfo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepo
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo

class ProductInfoViewModelFactory (private val repoProduct: ProductInfo = ProductInfo(), private val irepoFavourite: FavouriteLocal): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductInfoViewModel::class.java)){
            ProductInfoViewModel(repoProduct,irepoFavourite) as T
        }else{
            throw IllegalArgumentException("ViewModel class not found")
        }
    }
}