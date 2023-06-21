package com.mad43.stylista.data.remote.dataSource.order

import com.mad43.stylista.data.remote.entity.orders.ResponseOrders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.order.OrdersAPIInterface
import retrofit2.Response

class RemoteOrdersDataSourceImp(private val ordersAPIInterface: OrdersAPIInterface = ApiService.ordersAPIService) :
    RemoteOrdersDataSource {
    override suspend fun getAllOrders(id : Long): ResponseOrders {
        return ordersAPIInterface.getAllOrders(customerId = id.toString())
    }

    override suspend fun postOrder(postOrder : PostOrderResponse): Response<PostOrderResponse> {
        return ordersAPIInterface.postOrder(postOrder)
    }

}