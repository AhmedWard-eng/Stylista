package com.mad43.stylista.data.repo.address

import com.mad43.stylista.data.remote.dataSource.address.AddressRemoteDataSource
import com.mad43.stylista.data.remote.dataSource.address.AddressRemoteDataSourceImp
import com.mad43.stylista.data.remote.entity.address.allAddresses.AllAddresses
import com.mad43.stylista.data.remote.entity.address.oneAddress.CustomerAddress
import com.mad43.stylista.data.remote.entity.address.request.AddressRequest

class AddressRepoImp(private val addressRemoteDataSource: AddressRemoteDataSource = AddressRemoteDataSourceImp()) : AddressRepo {
    override suspend fun postNewAddress(customerId: String, body: AddressRequest): CustomerAddress {
        return addressRemoteDataSource.postNewAddress(customerId,body)
    }

    override suspend fun editExistedAddressWith(
        customerId: String,
        addressId: String,
        body: AddressRequest
    ): CustomerAddress {
        return addressRemoteDataSource.editExistedAddressWith(customerId,addressId,body)
    }

    override suspend fun makeThisAddressAsDefault(
        customerId: String,
        addressId: String
    ): CustomerAddress {
        return addressRemoteDataSource.makeThisAddressAsDefault(customerId,addressId)
    }

    override suspend fun getAddressesOfCustomer(customerId: String): AllAddresses {
        return addressRemoteDataSource.getAddressesOfCustomer(customerId)
    }
}