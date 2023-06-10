package com.mad43.stylista.data.remote.dataSource

import com.mad43.stylista.data.remote.entity.brand.BrandResponse
import com.mad43.stylista.data.remote.entity.product.ProductResponse
import com.mad43.stylista.data.remote.entity.product.ProductDetails

interface RemoteProductsDataSource {

    suspend fun getAllBrand(): BrandResponse
    suspend fun getAllProductInBrand(brand: String): ProductResponse
    suspend fun getProductDetails(id : Long) : ProductDetails

}