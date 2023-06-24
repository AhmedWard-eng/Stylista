package com.mad43.stylista.ui.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.domain.model.DisplayProduct
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.data.repo.product.ProductsRepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception

class BrandViewModel(private val repoInterface: ProductsRepoInterface = ProductsRepo()) :
    ViewModel() {

    var products = MutableStateFlow<RemoteStatus<List<DisplayProduct>>>(RemoteStatus.Loading)
    var dataFiltered: List<DisplayProduct> = listOf()
    var allData: List<DisplayProduct> = listOf()
    var filter = false
    var maxPrice = 0

    fun getProductInBrand(brand: String) {
        viewModelScope.launch {
            try {
                repoInterface.getAllProductInBrand(brand).catch { e ->
                    products.value = RemoteStatus.Failure(e)
                }.collect { data ->
                    products.value = RemoteStatus.Success(data)
                }
            } catch (e: Exception) {
                products.value = RemoteStatus.Failure(e)
            }
        }
    }

    fun seekMax() {
        allData.forEach {
            if (maxPrice.toFloat() < it.price.toFloat()) {
                maxPrice = it.price.toFloat().toInt() + 1
            }
        }
    }

    fun filterByPrice(price: String) {
        if (filter) {
            dataFiltered = allData.filter {
                (0.00 <= it.price.toFloat() && it.price.toFloat() <= price.toFloat())
            }
            if (price == "") {
                products.value = RemoteStatus.Success(allData)
            } else {
                products.value = RemoteStatus.Success(dataFiltered)
            }
        } else {
            products.value = RemoteStatus.Success(allData)
        }
    }

    fun displayAllProducts() {
        products.value = RemoteStatus.Success(allData)
    }

    fun filterByCategory(category: String) {
        if (filter) {
            dataFiltered = allData.filter {
                it.product_type == category
            }
            products.value = RemoteStatus.Success(dataFiltered)

        } else {
            products.value = RemoteStatus.Success(allData)
        }
    }


}