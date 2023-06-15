package com.mad43.stylista.data.remote.dataSource.product

import com.mad43.stylista.data.remote.entity.brand.BrandResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.data.remote.entity.product.ProductResponse

interface RemoteProductsDataSource {

    suspend fun getAllBrand(): BrandResponse
    suspend fun getAllProductInBrand(brand: String): ProductResponse

    suspend fun getProductById(id: Long): ProductDetails

    suspend fun getAllProduct(): ProductResponse


}