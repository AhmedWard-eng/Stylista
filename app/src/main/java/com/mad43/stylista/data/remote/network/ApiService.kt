package com.mad43.stylista.data.remote.network

import com.mad43.stylista.data.remote.network.auth.retrofit.LogInAPIInterface
import com.mad43.stylista.data.remote.network.auth.retrofit.SignUpAPIInterface
import com.mad43.stylista.data.remote.network.draftOrders.DraftOrdersAPIInterface
import com.mad43.stylista.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiService {

    val brandsApiService: ProductsAPIInterface = AppRetrofit.retrofit.create(ProductsAPIInterface::class.java)

    val draftAPIService : DraftOrdersAPIInterface = AppRetrofit.retrofit.create(DraftOrdersAPIInterface::class.java)

    object AppRetrofit {
        val retrofit: Retrofit =  Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    object APIClient{
        val signUpAPIService : SignUpAPIInterface by lazy {
            AppRetrofit.retrofit.create(SignUpAPIInterface::class.java)
        }
        val loginAPIService : LogInAPIInterface by lazy {
            AppRetrofit.retrofit.create(LogInAPIInterface::class.java)
        }
    }
}