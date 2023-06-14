package com.mad43.stylista.ui.search.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.data.repo.product.ProductsRepoInterface
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.domain.model.DisplayProduct
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(private val repoInterface : ProductsRepoInterface = ProductsRepo()) : ViewModel() {
    var brands = listOf<DisplayBrand>()
    var allPrpduct = listOf<DisplayProduct>()

    fun getBrand() {
        viewModelScope.launch {
            repoInterface.getAllBrand()
                .catch { e ->
                    Log.e(TAG, "Error fetching all brands: ${e.message}")
                }
                .collectLatest {
                    brands = it
                }
        }

    }
    fun searchBrand(query: String?): List<DisplayBrand> {
        return if (query.isNullOrBlank()) {
            brands
        } else {
            brands.filter { brand ->
                brand.title.startsWith(query,true)
            }
        }
    }

    fun getAllProduct(){
        viewModelScope.launch {
            repoInterface.getAllProduct()
                .catch { e ->
                    Log.e(TAG, "Error fetching all product in product: ${e.message}")
                }
                .collectLatest {
                    allPrpduct = it
                }
        }
    }

    fun searchAllProduct(query: String?): List<DisplayProduct> {
        return if (query.isNullOrBlank()) {
            allPrpduct
        } else {
            allPrpduct.filter { product ->
                product.title.startsWith(query,true)
            }
        }
    }

}