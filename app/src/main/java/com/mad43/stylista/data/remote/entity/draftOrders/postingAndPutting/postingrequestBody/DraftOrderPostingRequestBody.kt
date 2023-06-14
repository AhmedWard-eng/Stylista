package com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody

import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem

data class DraftOrderPostingRequestBody(
    val customer: Customer,
    val line_items: List<InsertingLineItem> = listOf(InsertingLineItem(listOf(),null, quantity = 1)),
    val note: String,
    val use_customer_default_address: Boolean = true
)