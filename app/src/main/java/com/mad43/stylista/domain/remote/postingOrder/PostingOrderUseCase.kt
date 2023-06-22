package com.mad43.stylista.domain.remote.postingOrder

import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.data.repo.order.OrdersRepo
import com.mad43.stylista.data.repo.order.OrdersRepoInterface
import com.mad43.stylista.util.RemoteStatus
import java.lang.Exception

class PostingOrderUseCase(private val ordersRepo: OrdersRepoInterface = OrdersRepo()) {

    suspend operator fun invoke(postOrderResponse: PostOrderResponse) : RemoteStatus<Boolean>{
        return try {
            val response = ordersRepo.postOrder(PostOrderResponse(postOrderResponse.order.copy(email = null, send_fulfillment_receipt = null, send_receipt = null, fulfillment_status = null)))
            val response2 = ordersRepo.postOrder(postOrderResponse)
            RemoteStatus.Success(true)
        }catch (e:Exception){
            RemoteStatus.Failure(Exception())
        }
    }
}