package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.util.RemoteStatus

class DeleteCartItemUseCase(private val cartRepo: CartRepo = CartRepoImp()) {
    suspend operator fun invoke(variantId : Long,cartId : Long): RemoteStatus<List<CartItem>> {
        val remoteStatus = cartRepo.getCartUsingId(cartId.toString())

        val draftOrder: DraftOrder?

        if (remoteStatus is RemoteStatus.Success) {
            draftOrder = remoteStatus.data.draft_order
        } else {
            return remoteStatus as RemoteStatus.Failure
        }

        val lineItems = draftOrder?.line_items

        val updatingList =
            removeAnItemFromCart(lineItems!!.toListOfPutLineItems(), variantId)


        val draftOrderPutBody = DraftOrderPutBody(DraftOrderPuttingRequestBody(updatingList))
        val theUpdatingRemoteStatus = cartRepo.updateCart(cartId, draftOrderPutBody)

        return if (theUpdatingRemoteStatus is RemoteStatus.Success) {
            val cartItems = theUpdatingRemoteStatus.data.draft_order?.line_items?.toCartItemList()

            if (cartItems == null) {
                RemoteStatus.Failure(Exception("null array"))
            } else {
                RemoteStatus.Success(
                    cartItems
                )
            }
        } else {
            theUpdatingRemoteStatus as RemoteStatus.Failure
        }
    }

    private fun removeAnItemFromCart(putLineItems: MutableList<InsertingLineItem>, variantId: Long): List<InsertingLineItem> {
        val theUpdatingProduct = putLineItems.find {it.variant_id == variantId }

        if (theUpdatingProduct != null) {
            putLineItems.remove(theUpdatingProduct)
        }

        return putLineItems.toList()
    }
}