package com.mad43.stylista.ui.search.viewModel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.data.repo.ProductsRepo
import com.mad43.stylista.data.repo.ProductsRepoInterface
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.domain.model.DisplayProduct
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SearchViewModel(private val repoInterface : ProductsRepoInterface = ProductsRepo()) : ViewModel() {
    var brands = listOf<DisplayBrand>()
    var prpduct = listOf<DisplayProduct>()
    var allPrpduct = listOf<DisplayProduct>()
    init {
        getAllProduct()
    }
    fun getBrand() {
        viewModelScope.launch {
            repoInterface.getAllBrand()
                .catch { e ->
                    Log.e(TAG, "Error fetching all brands: ${e.message}")
                }
                .collectLatest {
                    brands = it
                    Log.e(TAG, "brands ${brands.size}")
                }
        }

    }
    fun getProduct(brand: String){
        viewModelScope.launch {
            repoInterface.getAllProductInBrand(brand)
                .catch { e ->
                    Log.e(TAG, "Error fetching all product in brand: ${e.message}")
                }
                .collectLatest {
                    prpduct = it
                    Log.e(TAG, "brands ${prpduct.size}")
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

    fun searchProduct(query: String?): List<DisplayProduct> {
        return if (query.isNullOrBlank()) {
            prpduct
        } else {
            prpduct.filter { product ->
                product.title.contains(query,true)
            }
        }
    }

    fun getAllProduct(){
        viewModelScope.launch {
            repoInterface.getAllProduct()
                .catch { e ->
                    Log.e(TAG, "Error fetching all product in brand: ${e.message}")
                }
                .collectLatest {
                    allPrpduct = it
                    Log.e(TAG, "brands ${allPrpduct.size}")
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