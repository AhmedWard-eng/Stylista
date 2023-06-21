package com.mad43.stylista.domain.remote.cart

import android.util.Log
import com.mad43.stylista.data.remote.entity.draftOrders.Property
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPuttingRequestBody
import com.mad43.stylista.data.repo.cart.CartRepo
import com.mad43.stylista.data.repo.cart.CartRepoImp
import com.mad43.stylista.domain.model.CartItem
import com.mad43.stylista.domain.model.PuttingCartItem
import com.mad43.stylista.domain.model.toCartItemList
import com.mad43.stylista.domain.model.toInsertingLineItemList
import com.mad43.stylista.util.RemoteStatus
import kotlin.math.log

class EditCartItemUseCase (private val cartRepo: CartRepo = CartRepoImp()) {

    suspend operator fun invoke(variantId : Long, quantity : Int ,cartId : Long,cartItemList : List<CartItem>): RemoteStatus<List<CartItem>> {

        val updatingList =
            updateQuantityOfItem(cartItemList.toInsertingLineItemList().toMutableList(), variantId,quantity)

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
        Log.d("TAG", "updateQuantityOfItem: $putLineItems")
        val theUpdatingProduct = putLineItems.find {it.variant_id == variantId }
        if (theUpdatingProduct != null) {
            putLineItems[putLineItems.indexOf(theUpdatingProduct)] = theUpdatingProduct.copy(quantity = quantity)
            putLineItems.add(InsertingLineItem(properties = listOf(),variant_id = null,quantity = 1))
        }
        return putLineItems.toList()
    }
}