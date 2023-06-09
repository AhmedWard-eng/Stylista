package com.mad43.stylista.data.repo.product

import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.domain.model.DisplayProduct
import kotlinx.coroutines.flow.Flow

interface ProductsRepoInterface {
    suspend fun getAllBrand() : Flow<List<DisplayBrand>>
    suspend fun getAllProductInBrand(brand: String) : Flow<List<DisplayProduct>>
    suspend fun getProductDetails(id: Long): Flow<ProductDetails>
    suspend fun getAllProduct() :  Flow<List<DisplayProduct>>

}