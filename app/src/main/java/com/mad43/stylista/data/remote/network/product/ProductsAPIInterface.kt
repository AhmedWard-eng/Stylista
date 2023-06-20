package com.mad43.stylista.data.remote.network.product

import com.mad43.stylista.data.remote.entity.brand.BrandResponse
import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.data.remote.entity.product.ProductResponse
import com.mad43.stylista.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface ProductsAPIInterface {
    @GET("smart_collections.json")
    suspend fun getAllBrand(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): BrandResponse

    @GET("products.json")
    suspend fun getAllProducts(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): ProductResponse

    @GET("products/" + "{id}" + ".json")
    suspend fun getProductDetails(
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
        @Path("id") id: Long,

        ): ProductDetails
}