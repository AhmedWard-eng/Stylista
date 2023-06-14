package com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting

import com.mad43.stylista.data.remote.entity.draftOrders.Property

data class InsertingLineItem(
    val properties: List<Property>?,
    val variant_id: Long?,
    val price: String? = "1",
    val quantity: Int?,
    val title: String? = "dummy"
)