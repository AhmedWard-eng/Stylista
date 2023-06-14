package com.mad43.stylista.domain.remote.cart

import android.util.Log
import com.mad43.stylista.data.remote.entity.draftOrders.LineItem
import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.util.RemoteStatus

class GetCartListUseCase(private val cartRepo: CartRepo = CartRepoImp()) {
    suspend operator fun invoke(cartId: String): RemoteStatus<List<CartItem>> {
        val remoteStatus = cartRepo.getCartUsingId(cartId)
        return if (remoteStatus is RemoteStatus.Success) {
            val lineItem = remoteStatus.data.draft_order?.line_items
            Log.d("TAG", "invoke: $lineItem")
            if (!lineItem.isNullOrEmpty())
                RemoteStatus.Success(lineItem.toCartItemList())
            else {
                RemoteStatus.Failure(Exception("Null Array"))
            }
        } else {
            (remoteStatus as RemoteStatus.Failure)
        }
    }
}