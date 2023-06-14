package com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody

import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem

data class DraftOrderPuttingRequestBody(
    val line_items: List<InsertingLineItem>
)