package com.mad43.stylista.data.repo.order

import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface OrdersRepoInterface {
    suspend fun getAllOrders() : Flow<List<Orders>>

    suspend fun postOrder(postOrder : PostOrderResponse) : Response<PostOrderResponse>

}