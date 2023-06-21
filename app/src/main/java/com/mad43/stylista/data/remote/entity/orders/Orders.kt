package com.mad43.stylista.data.remote.entity.orders

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Orders(
    val id: Long? = 0,
    val created_at: String? = null,
    val currency: String? = null,
    val current_subtotal_price: String? = null,
    val current_total_discounts: String? = null,
    val current_total_price: String? = null,
    val discount_codes: List<DiscountCode>?,
    val email: String?,
    val number: Int? = null,
    @SerializedName("name")
    val name: String? = null,
    @SerializedName("line_items")
    val lineItems: List<LineItems>?,
) : Parcelable
