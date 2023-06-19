package com.mad43.stylista.data.remote.entity.orders

import com.google.gson.annotations.SerializedName

data class Orders(
    val id: Long? = 0,
    val created_at: String? = null,
    val currency: String? = null,
    val current_subtotal_price: String? = null,
    val current_total_discounts: String? = null,
    val current_total_price: String? = null,
    val discount_codes: List<DiscountCode>?,
    var email: String?,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("line_items")
    val lineItems: List<LineItems>?
    )
