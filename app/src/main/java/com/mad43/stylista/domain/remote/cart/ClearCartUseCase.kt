package com.mad43.stylista.domain.remote.cart

import android.util.Log
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.util.RemoteStatus

class ClearCartUseCase(private val cartRepo: CartRepo = CartRepoImp()) {

    suspend operator fun invoke(cartId : Long): RemoteStatus<Boolean> {

        val draftOrderPutBody = DraftOrderPutBody(
            DraftOrderPuttingRequestBody(
                getList()
            )
        )

        val theUpdatingRemoteStatus = cartRepo.updateCart(cartId, draftOrderPutBody)

        return if (theUpdatingRemoteStatus is RemoteStatus.Success) {
            val cartItems = theUpdatingRemoteStatus.data.draft_order?.line_items?.toCartItemList()

            if (cartItems == null) {
                RemoteStatus.Failure(Exception("null array"))
            } else {
                RemoteStatus.Success(true)
            }
        } else {
            theUpdatingRemoteStatus as RemoteStatus.Failure
        }
    }

    private fun getList(): List<InsertingLineItem> {
        val putLineItems: MutableList<InsertingLineItem> = mutableListOf()
        putLineItems.add(InsertingLineItem(properties = listOf(), variant_id = null, quantity = 1))
        return putLineItems.toList()
    }
}