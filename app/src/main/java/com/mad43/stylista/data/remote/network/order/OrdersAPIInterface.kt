package com.mad43.stylista.data.remote.network.order

import com.mad43.stylista.data.remote.entity.orders.ResponseOrders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface OrdersAPIInterface {
    @GET("customers/{customerId}/orders.json")
    suspend fun getAllOrders(
        @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD,
        @Path("customerId") customerId: String
    ): ResponseOrders

    @POST("orders.json")
    suspend fun postOrder(
        @Body order: PostOrderResponse,
        @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD,
    ): PostOrderResponse
}