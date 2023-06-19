package com.mad43.stylista.domain.model

import android.os.Parcelable
import com.mad43.stylista.data.remote.entity.address.allAddresses.Addresse
import com.mad43.stylista.data.remote.entity.address.request.Address
import com.mad43.stylista.data.remote.entity.address.request.AddressRequest
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressItem(
    val customerId: Long,
    val addressId: Long = -1,
    val address1: String = "",
    val address2: String = "",
    val city: String = "",
    val country: String = "",
    val phone: String = "",
    val province: String = "",
    val isDefault: Boolean = false
) : Parcelable

fun AddressItem.toAddressRequest(isDefault :Boolean?) : AddressRequest{
    return AddressRequest(
        Address(
            address1 = address1,
            address2 = address2,
            city = city,
            country = country,
            default = isDefault,
            phone = phone,
            company = null,
            first_name = null,
            last_name = null,
            name = null,
            province = province
        )
    )
}

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
