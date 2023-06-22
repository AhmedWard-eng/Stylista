package com.mad43.stylista.data.remote.dataSource.order

import com.mad43.stylista.data.remote.entity.orders.ResponseOrders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import retrofit2.Response

interface RemoteOrdersDataSource {

    suspend fun getAllOrders(id : Long): ResponseOrders

    suspend fun postOrder(postOrder : PostOrderResponse) : PostOrderResponse
}