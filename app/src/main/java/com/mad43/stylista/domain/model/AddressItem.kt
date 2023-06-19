package com.mad43.stylista.domain.model

import android.os.Parcelable
import com.mad43.stylista.data.remote.entity.address.allAddresses.Addresse
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressItem(
    val customerId : Long,
    val addressId: Long,
    val address1: String,
    val address2: String,
    val city: String,
    val country: String,
    val phone: String,
    val province: String,
    val isDefault : Boolean
) : Parcelable

fun List<Addresse?>.toAddressItemList(): List<AddressItem> {
    return map {
        AddressItem(
            customerId = it?.customer_id ?: 0L,
            addressId = it?.id ?: 0L,
            address1 = it?.address1 ?: "",
            address2 = it?.address2 ?: "",
            city = it?.city ?: "",
            phone = it?.phone ?: "",
            country = it?.country ?: "",
            province = it?.province ?: "",
            isDefault = it?.default ?: false
        )
    }
}
