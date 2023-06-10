package com.mad43.stylista.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.repo.ProductsRepo
import com.mad43.stylista.data.repo.ProductsRepoInterface
import com.mad43.stylista.domain.model.DisplayProduct
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class CategoryViewModel(private val repoInterface: ProductsRepoInterface = ProductsRepo()) :
    ViewModel() {

    var products = MutableStateFlow<RemoteStatus<List<DisplayProduct>>>(RemoteStatus.Loading)
    lateinit var productMainCategory: List<DisplayProduct>
    lateinit var productSubCategory: List<DisplayProduct>
    lateinit var allData: List<DisplayProduct>
    var filterMainCategory = false
    var filterSubCategory = false
    var sub : String? = null

    init {
        getProducts()
    }
    private fun getProducts() {
        viewModelScope.launch {
            repoInterface.getAllProducts().catch {
                    e -> products.value = RemoteStatus.Failure(e)
            }.collect {
                    data -> products.value = RemoteStatus.Success(data)
            }
        }
    }

    fun filterByMainCategory(mainCategory: String){
        if (filterMainCategory){
            productMainCategory = allData.filter {
                it.product_type == mainCategory
            }
            products.value = RemoteStatus.Success(productMainCategory)
        }else{
            products.value = RemoteStatus.Success(allData)
        }
    }

    fun filterBySubCategory(subCategory: String){
        if (filterMainCategory){
            if (filterSubCategory){
                productSubCategory = productMainCategory.filter {
                    val strs = it.tag.split(",").toTypedArray()
                    strs.forEach {
                        if (it == subCategory ){
                            sub = it
                        }
                    }
                    sub == subCategory
                }
                products.value = RemoteStatus.Success(productSubCategory)
            }
            else{
                products.value = RemoteStatus.Success(productMainCategory)
            }
        }else{
            products.value = RemoteStatus.Success(allData)
        }
    }
}