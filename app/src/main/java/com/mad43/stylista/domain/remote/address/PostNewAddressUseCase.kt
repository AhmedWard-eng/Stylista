package com.mad43.stylista.domain.remote.address

import com.mad43.stylista.data.repo.address.AddressRepo
import com.mad43.stylista.data.repo.address.AddressRepoImp
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.toAddressRequest
import com.mad43.stylista.util.RemoteStatus

class PostNewAddressUseCase(private val addressRepo: AddressRepo = AddressRepoImp()) {

    suspend operator fun invoke(addressItem: AddressItem) : RemoteStatus<Boolean>{
        return try {
            addressRepo.postNewAddress(addressItem.customerId.toString(),addressItem.toAddressRequest(addressItem.isDefault))
            RemoteStatus.Success(true)
        }catch (e : Exception){
            RemoteStatus.Failure(e)
        }
    }
}

