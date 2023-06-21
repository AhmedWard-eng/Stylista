package com.mad43.stylista.data.remote.entity.orders

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.mad43.stylista.data.remote.entity.draftOrders.Property
import kotlinx.parcelize.Parcelize

@Parcelize
data class LineItems(
    @SerializedName("id")
    val id: Long? = 0L,
    val name: String? = "",
    @SerializedName("price")
    val price: String? = "",
    @SerializedName("quantity")
    val quantity: Int? = 0,
    val properties: List<Property>?,
    val product_id: Long?,
    @SerializedName("variant_id")
    val variantId: Long? = 0L,
) : Parcelable
