package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.entity.brand.BrandResponse
import com.mad43.stylista.data.remote.entity.product.ProductResponse
import com.mad43.stylista.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header

interface ProductsAPIInterface {

    @GET("smart_collections.json")
    suspend fun getAllBrand(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): BrandResponse

    @GET("products.json")
    suspend fun getAllProducts(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): ProductResponse
}