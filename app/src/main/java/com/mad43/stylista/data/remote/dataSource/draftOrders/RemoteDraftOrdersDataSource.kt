package com.mad43.stylista.data.remote.dataSource.draftOrders

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.AllDraftOrders
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse

interface RemoteDraftOrdersDataSource {

    suspend fun postDraftOrder(draftOrderRequestBody: DraftOrderRequestBody): DraftOrderResponse

    suspend fun putDraftOrder(
        id: Long,
        draftOrderPutBody: DraftOrderPutBody
    ): DraftOrderResponse

    suspend fun getAllDraftOrders(): AllDraftOrders

    suspend fun getDraftOrderById(id: String): CustomDraftOrderResponse

}