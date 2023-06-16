package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.util.RemoteStatus

interface FavouriteRepository {
    suspend fun createFavouriteForCustomer(customerId : Long): RemoteStatus<Long>


}