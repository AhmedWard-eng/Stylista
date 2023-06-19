package com.mad43.stylista.data.repo.address

import com.mad43.stylista.data.remote.entity.address.allAddresses.AllAddresses
import com.mad43.stylista.data.remote.entity.address.oneAddress.CustomerAddress
import com.mad43.stylista.data.remote.entity.address.request.AddressRequest

interface AddressRepo {
    suspend fun postNewAddress(customerId: String, body: AddressRequest): CustomerAddress

    suspend fun editExistedAddressWith(
        customerId: String,
        addressId: String,
        body: AddressRequest
    ): CustomerAddress

    suspend fun makeThisAddressAsDefault(customerId: String, addressId: String): CustomerAddress

    suspend fun getAddressesOfCustomer(customerId: String): AllAddresses
}