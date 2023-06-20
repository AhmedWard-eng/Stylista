package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.util.RemoteStatus
import kotlin.Exception

class GetAvailableQuantityOfVariantUseCase(private val cartRepo: CartRepo = CartRepoImp()) {

    suspend operator fun invoke(variantId : Long,requiredQuantity : Int) : RemoteStatus<Boolean>{
        return try {
            val product = cartRepo.getAllProduct().find { product -> product.variants.find { variantId == it.id } != null}
            val variant = product?.variants?.find { variant -> variant.id == variantId}
            val availableQuantity = variant?.inventory_quantity
            if(availableQuantity != null) {
                val canUpdate = canUpdate(requiredQuantity,availableQuantity)
                RemoteStatus.Success(canUpdate)
            }
            else
                RemoteStatus.Failure(Exception("quantity is null"))
        }catch (e: Exception){
            RemoteStatus.Failure(e)
        }
    }

    private fun canUpdate(requiredQuantity: Int, availableQuantity: Int): Boolean {
        return if(availableQuantity < 4){
            requiredQuantity <= availableQuantity
        }else if((availableQuantity / 3) < 4){
            requiredQuantity < 4
        }else{
            requiredQuantity < (availableQuantity / 3)
        }
    }

}