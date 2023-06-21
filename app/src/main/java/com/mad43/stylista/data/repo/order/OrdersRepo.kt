package com.mad43.stylista.data.repo.order

import com.mad43.stylista.data.remote.dataSource.order.RemoteOrdersDataSource
import com.mad43.stylista.data.remote.dataSource.order.RemoteOrdersDataSourceImp
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class OrdersRepo(
    private val remoteOrdersDataSource: RemoteOrdersDataSource = RemoteOrdersDataSourceImp(),
    private val preferencesData: PreferencesData = PreferencesData(),
) :
    OrdersRepoInterface {

    private var customerId : Long? = null
    override suspend fun getAllOrders(): Flow<List<Orders>> {
        preferencesData.getCustomerData().onSuccess {
            customerId = it.customerId
        }

        return flowOf(remoteOrdersDataSource.getAllOrders(customerId ?: 0L).orders) as Flow<List<Orders>>
    }

    override suspend fun postOrder(postOrder: PostOrderResponse): Response<PostOrderResponse> {
        return remoteOrdersDataSource.postOrder(postOrder)
    }
}