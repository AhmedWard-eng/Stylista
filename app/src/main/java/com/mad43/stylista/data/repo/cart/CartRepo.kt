package com.mad43.stylista.data.repo.cart

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.AllDraftOrders
import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.util.RemoteStatus
import retrofit2.Response

interface CartRepo {

    suspend fun createCartForCustomer(customerId : Long): RemoteStatus<Long>

    suspend fun updateCart(id: Long, draftOrderPutBody: DraftOrderPutBody): RemoteStatus<DraftOrderResponse>

    suspend fun getCartUsingId(id: String): RemoteStatus<CustomDraftOrderResponse>

    suspend fun getCartWithEmail(email : String) : RemoteStatus<DraftOrder>
    suspend fun getAllProduct(): List<Product>
}