package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.remote.entity.draftOrders.Property
import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.PuttingCartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.util.RemoteStatus

class EditCartItemUseCase (private val cartRepo: CartRepo = CartRepoImp()) {

    suspend operator fun invoke(variantId : Long, quantity : Int , email : String,cartId : Long): RemoteStatus<List<CartItem>> {
        val remoteStatus = cartRepo.getCartWithEmail(email)
        val draftOrder: DraftOrder
        if (remoteStatus is RemoteStatus.Success) {
            draftOrder = remoteStatus.data
        } else {
            return remoteStatus as RemoteStatus.Failure
        }
        val lineItems = draftOrder.line_items

        val updatingList =
            updateQuantityOfItem(lineItems!!.toListOfPutLineItems(), variantId,quantity)

        val draftOrderPutBody = DraftOrderPutBody(
            DraftOrderPuttingRequestBody(
                updatingList
            )
        )
        val theUpdatingRemoteStatus = cartRepo.updateCart(cartId, draftOrderPutBody)

        return if (theUpdatingRemoteStatus is RemoteStatus.Success) {
            val cartItems = theUpdatingRemoteStatus.data.draft_order?.line_items?.toCartItemList()

            if (cartItems == null) {
                RemoteStatus.Failure(Exception("null array"))
            } else {
                RemoteStatus.Success(cartItems)
            }
        } else {
            theUpdatingRemoteStatus as RemoteStatus.Failure
        }
    }

    private fun updateQuantityOfItem(putLineItems: MutableList<InsertingLineItem>, variantId: Long, quantity: Int): List<InsertingLineItem> {
        val theUpdatingProduct = putLineItems.find { it.variant_id == variantId }

    }

    private fun updateThisListWithNewItem(
        lineItems: MutableList<InsertingLineItem>,
        puttingCartItem: PuttingCartItem
    ): List<InsertingLineItem> {
        lineItems.add(
            InsertingLineItem(
                properties = listOf(Property(value = puttingCartItem.imageUrl)),
                variant_id = puttingCartItem.variantId,
                quantity = puttingCartItem.quantity
            )
        )
        return lineItems.toList()
    }
}