package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.network.auth.AuthAPIInterface
import com.mad43.stylista.data.remote.network.draftOrders.DraftOrdersAPIInterface
import com.mad43.stylista.data.remote.network.product.ProductsAPIInterface
import com.mad43.stylista.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {

    val authApiService: AuthAPIInterface = AppRetrofit.retrofit.create(AuthAPIInterface::class.java)

    val brandsApiService: ProductsAPIInterface = AppRetrofit.retrofit.create(ProductsAPIInterface::class.java)

    val draftAPIService : DraftOrdersAPIInterface = AppRetrofit.retrofit.create(DraftOrdersAPIInterface::class.java)

    object AppRetrofit {
        val retrofit: Retrofit =  Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}