package com.mad43.stylista.data.remote.entity.orders

import com.google.gson.annotations.SerializedName

data class LineItems(
    @SerializedName("id")
    val id: Long? = 0L,
    val name: String? = "",
    @SerializedName("price")
    val price: String? ="",
    @SerializedName("quantity")
    val quantity: Int? =0,
    @SerializedName("variant_id")
    val variantId: Long? = 0L,
)
