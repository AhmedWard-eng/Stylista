package com.mad43.stylista.ui.brand

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.repo.ProductsRepo
import com.mad43.stylista.domain.model.DisplayProduct
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.data.repo.ProductsRepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class BrandViewModel(private val repoInterface: ProductsRepoInterface = ProductsRepo()) :
    ViewModel() {

    var products = MutableStateFlow<RemoteStatus<List<DisplayProduct>>>(RemoteStatus.Loading)
    lateinit var dataFiltered: List<DisplayProduct>
    lateinit var allData: List<DisplayProduct>
    var filter = false

    fun getProductInBrand(brand: String) {
        viewModelScope.launch {
            repoInterface.getAllProductInBrand(brand).catch { e ->
                products.value = RemoteStatus.Failure(e)
            }.collect { data ->
                products.value = RemoteStatus.Success(data)
            }
        }
    }

    fun filterByPrice(price: String) {
        if (filter) {
            dataFiltered = allData.filter {
                it.price == price
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