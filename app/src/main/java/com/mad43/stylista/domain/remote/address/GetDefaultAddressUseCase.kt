package com.mad43.stylista.domain.remote.address

import com.mad43.stylista.data.repo.address.AddressRepo
import com.mad43.stylista.data.repo.address.AddressRepoImp
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.model.toAddressItemList
import com.mad43.stylista.util.RemoteStatus

class GetDefaultAddressUseCase (private val addressRepo: AddressRepo = AddressRepoImp()) {
    suspend operator fun invoke(customerId : String): RemoteStatus<AddressItem> {
        return try {
            val addresses = addressRepo.getAddressesOfCustomer(customerId).addresses
            if(addresses != null){
                if (addresses.isEmpty()){
                     RemoteStatus.Failure(HasNoAddressException())
                }else{
                    val defaultAddress = addresses.toAddressItemList().find{ it.isDefault}
                    if(defaultAddress != null){
                        RemoteStatus.Success(defaultAddress)
                    }else{
                        RemoteStatus.Failure(Exception("Null Array"))
                    }
                }

            }else{
                RemoteStatus.Failure(Exception("Null Array"))
            }
        }catch (e : Exception){
            RemoteStatus.Failure(e)
        }
    }
}

class HasNoAddressException : Exception()