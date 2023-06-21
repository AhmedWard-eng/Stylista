package com.mad43.stylista.data.remote.dataSource.address

import com.mad43.stylista.data.remote.entity.address.allAddresses.AllAddresses
import com.mad43.stylista.data.remote.entity.address.oneAddress.CustomerAddress
import com.mad43.stylista.data.remote.entity.address.request.AddressRequest
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.address.AddressApiInterface
import retrofit2.Response

class AddressRemoteDataSourceImp(private val addressApiInterface: AddressApiInterface = ApiService.addressApiInterface): AddressRemoteDataSource {
    override suspend fun postNewAddress(customerId: String, body: AddressRequest): CustomerAddress {
       return addressApiInterface.postNewAddress(customerId,body)
    }

    override suspend fun editExistedAddressWith(
        customerId: String,
        addressId: String,
        body: AddressRequest
    ): CustomerAddress {
        return addressApiInterface.editExistedAddressWith(customerId,addressId,body)
    }

    override suspend fun makeThisAddressAsDefault(
        customerId: String,
        addressId: String
    ): CustomerAddress {
        return addressApiInterface.makeThisAddressAsDefault(customerId,addressId )
    }

    override suspend fun getAddressesOfCustomer(customerId: String): AllAddresses {
        return addressApiInterface.getAddressesOfCustomer(customerId)
    }

    override suspend fun deleteAddressOfCustomerWithId(
        customerId: String,
        addressId: String
    ): Response<Any> {
        return addressApiInterface.deleteAddressOfCustomerWithId(customerId,addressId)
    }
}