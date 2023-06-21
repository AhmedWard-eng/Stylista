package com.mad43.stylista.domain.remote.address

import com.mad43.stylista.data.repo.address.AddressRepo
import com.mad43.stylista.data.repo.address.AddressRepoImp
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.toAddressItemList
import com.mad43.stylista.util.RemoteStatus

class DeleteAnAddressUseCase(private val addressRepo: AddressRepo = AddressRepoImp()) {
    suspend operator fun invoke(customerId : String,addressId : String): RemoteStatus<Boolean> {
       return try {
           val result = addressRepo.deleteAddressOfCustomerWithId(customerId,addressId)

           if (result.isSuccessful){
               RemoteStatus.Success(true)
           }else{
               RemoteStatus.Failure(Exception("network is failed"))
           }
        }catch (e : Exception){
           RemoteStatus.Failure(e)
        }
    }
}