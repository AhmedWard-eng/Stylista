package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.util.RemoteStatus

class GetCartIdUseCase (private val cartRepo: CartRepo = CartRepoImp()) {
    suspend operator fun invoke(email: String): RemoteStatus<Long> {
        val remoteStatus = cartRepo.getCartWithEmail(email)
        return if (remoteStatus is RemoteStatus.Success) {
            val id = remoteStatus.data.id
            if (id != null)
                RemoteStatus.Success(id.toLong())
            else {
                RemoteStatus.Failure(Exception("Null Array"))
            }
        } else {
            (remoteStatus as RemoteStatus.Failure)
        }
    }
}