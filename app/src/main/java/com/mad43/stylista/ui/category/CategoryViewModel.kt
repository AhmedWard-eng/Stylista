package com.mad43.stylista.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.R
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.data.repo.product.ProductsRepoInterface

import com.mad43.stylista.domain.model.DisplayProduct
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoryViewModel(private val repoInterface: ProductsRepoInterface = ProductsRepo()) :
    ViewModel() {

    var products = MutableStateFlow<RemoteStatus<List<DisplayProduct>>>(RemoteStatus.Loading)
    lateinit var productMainCategory: List<DisplayProduct>
    private var productSubCategory: MutableList<DisplayProduct> = mutableListOf()
    lateinit var allData: List<DisplayProduct>
    var filterMainCategory = false
    var filterSubCategory = false

    init {

        getProducts()
    }

    private fun getProducts() {
        viewModelScope.launch {
            repoInterface.getAllProduct().catch { e ->
                products.value = RemoteStatus.Failure(e)
            }.collect { data ->
                products.value = RemoteStatus.Success(data)
            }
        }
    }

    fun filterByMainCategory(mainCategory: String) {
        if (filterMainCategory) {
            productMainCategory = allData.filter {
                it.product_type == mainCategory
            }
            products.value = RemoteStatus.Success(productMainCategory)
        } else {
            products.value = RemoteStatus.Success(allData)
        }
    }

    fun filterBySubCategory(subCategory: String) {

        if (filterMainCategory) {
            if (filterSubCategory) {
                productSubCategory.clear()
                productMainCategory.forEach {product->
                    val strings = product.tag.split(",")
                    strings.forEach {
                        if (subCategory.trim().equals(it.trim(), true)) {
                            productSubCategory.add(product)
                        }
                    }
                }
                products.value = RemoteStatus.Success(productSubCategory)
            } else {
                products.value = RemoteStatus.Success(productMainCategory)
            }
        } else {
            products.value = RemoteStatus.Success(allData)
        }
    }
}