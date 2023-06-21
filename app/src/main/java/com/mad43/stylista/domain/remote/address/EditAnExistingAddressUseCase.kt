package com.mad43.stylista.domain.remote.address

import com.mad43.stylista.data.repo.address.AddressRepo
import com.mad43.stylista.data.repo.address.AddressRepoImp
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.toAddressRequest
import com.mad43.stylista.util.RemoteStatus

class EditAnExistingAddressUseCase(private val addressRepo: AddressRepo = AddressRepoImp()) {

    suspend operator fun invoke(addressItem: AddressItem) : RemoteStatus<Boolean> {
        return try {
            addressRepo.editExistedAddressWith(addressItem.customerId.toString(), addressId = addressItem.addressId.toString(),addressItem.toAddressRequest(null))
            RemoteStatus.Success(true)
        }catch (e : Exception){
            RemoteStatus.Failure(e)
        }
    }
}