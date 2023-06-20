package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    suspend fun createFavouriteForCustomer(customerId : Long): RemoteStatus<Long>

    suspend fun  insertFavouriteForCustomer(idFavourite: Long,draftOrderPutBody: DraftOrderPutBody): RemoteStatus<DraftOrderResponse>

    suspend fun getFavouriteUsingId(id: String):RemoteStatus<CustomDraftOrderResponse>

}