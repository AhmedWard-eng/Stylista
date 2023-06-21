package com.mad43.stylista.data.remote.dataSource.address

import com.mad43.stylista.data.remote.entity.address.allAddresses.AllAddresses
import com.mad43.stylista.data.remote.entity.address.oneAddress.CustomerAddress
import com.mad43.stylista.data.remote.entity.address.request.AddressRequest
import com.mad43.stylista.data.remote.entity.auth.SignupResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface AddressRemoteDataSource {

    suspend fun postNewAddress(customerId: String, body: AddressRequest): CustomerAddress

    suspend fun editExistedAddressWith(
        customerId: String,
        addressId: String,
        body: AddressRequest
    ): CustomerAddress

    suspend fun makeThisAddressAsDefault(customerId: String, addressId: String): CustomerAddress

    suspend fun getAddressesOfCustomer(customerId: String): AllAddresses

    suspend fun deleteAddressOfCustomerWithId( customerId : String,addressId :String): Response<Any>
}