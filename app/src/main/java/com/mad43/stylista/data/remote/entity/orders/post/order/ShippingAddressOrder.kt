package com.mad43.stylista.data.remote.entity.orders.post.order

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShippingAddressOrder(
    val address1: String?,
    val address2 : String?,
    val phone: String?,
    val city: String?,
    val province: String?,
    val country: String?,
    val zip: String?,
) : Parcelable
