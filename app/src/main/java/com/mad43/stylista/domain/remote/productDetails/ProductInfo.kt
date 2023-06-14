package com.mad43.stylista.domain.remote.productDetails


import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.data.repo.product.ProductsRepoInterface
import kotlinx.coroutines.flow.Flow

class ProductInfo (val repo : ProductsRepoInterface = ProductsRepo()){
    suspend fun getProductDetails(id: Long): Flow<ProductDetails> {
        return repo.getProductDetails(id)
    }
}