package com.mad43.stylista.data.remote.network.address

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

interface AddressApiInterface {
    @POST("customers/{customerId}/addresses.json")
    suspend fun postNewAddress(
        @Path("customerId") customerId : String,
        @Body body: AddressRequest,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD,
    ): CustomerAddress

    @PUT("customers/{customerId}/addresses/{addressId}.json")
    suspend fun editExistedAddressWith(
        @Path("customerId") customerId : String,
        @Path("addressId") addressId :String,
        @Body body: AddressRequest,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD
    ): CustomerAddress

    @PUT("customers/{customerId}/addresses/{addressId}/default.json")
    suspend fun makeThisAddressAsDefault(
        @Path("customerId") customerId : String,
        @Path("addressId") addressId :String,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD
    ): CustomerAddress


    @GET("customers/{customerId}/addresses.json")
    suspend fun getAddressesOfCustomer(
        @Path("customerId") customerId : String,
        @Header("X-Shopify-Access-Token") password: String = Constants.PASSWORD
    ): AllAddresses
}