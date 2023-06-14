package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.util.RemoteStatus

class CreateCartUseCase(private val cartRepo: CartRepo = CartRepoImp()) {

    suspend operator fun invoke(customerId : Long) : RemoteStatus<Long>{
        return cartRepo.createCartForCustomer(customerId)
    }
}