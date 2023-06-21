package com.mad43.stylista.domain.remote.address

import com.mad43.stylista.data.repo.address.AddressRepo
import com.mad43.stylista.data.repo.address.AddressRepoImp
import com.mad43.stylista.util.RemoteStatus

class SetAddressAsDefaultUseCase(private val addressRepo: AddressRepo= AddressRepoImp()) {

    suspend operator fun  invoke(customerId:String,addressId:String): RemoteStatus<Boolean> {
        return try{
            addressRepo.makeThisAddressAsDefault(customerId, addressId)
            RemoteStatus.Success(true)
        }catch (e:Exception){
            RemoteStatus.Failure(e)
        }
    }
}