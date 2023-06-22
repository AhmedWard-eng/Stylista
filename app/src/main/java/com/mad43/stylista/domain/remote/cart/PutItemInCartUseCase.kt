package com.mad43.stylista.domain.remote.cart

import com.mad43.stylista.data.remote.entity.draftOrders.LineItem
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
import com.mad43.stylista.ui.cart.CantUpdateException
import com.mad43.stylista.util.RemoteStatus

class PutItemInCartUseCase(
    private val cartRepo: CartRepo = CartRepoImp(),
    private val checkTheAbilityOfIncreasingVariantInUseCase: CheckTheAbilityOfIncreasingVariantInUseCase = CheckTheAbilityOfIncreasingVariantInUseCase()
) {

    suspend operator fun invoke(puttingCartItem: PuttingCartItem): RemoteStatus<List<CartItem>> {
        val remoteStatus = cartRepo.getCartUsingId(puttingCartItem.cartId.toString())
        val draftOrder: DraftOrder?
        if (remoteStatus is RemoteStatus.Success) {
            draftOrder = remoteStatus.data.draft_order
        } else {
            return remoteStatus as RemoteStatus.Failure
        }
        val lineItems = draftOrder?.line_items

        val isExisted = lineItems?.find { it.variant_id == puttingCartItem.variantId } != null

        if (isExisted){
            return RemoteStatus.Failure(AlreadyAddedToCartException())
        }

        val updatingList =
            updateThisListWithNewItem(
                lineItems!!.toListOfPutLineItems(),
                puttingCartItem
            )

        val draftOrderPutBody = DraftOrderPutBody(
            DraftOrderPuttingRequestBody(
                updatingList
            )
        )
        val theUpdatingRemoteStatus = cartRepo.updateCart(puttingCartItem.cartId, draftOrderPutBody)

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

    private fun updateThisListWithNewItem(
        lineItems: MutableList<InsertingLineItem>,
        puttingCartItem: PuttingCartItem
    ): List<InsertingLineItem> {
//        if (existed) {
//            val item = lineItems.find { it.variant_id == puttingCartItem.variantId }
//            if (item != null) {
//                lineItems[lineItems.indexOf(item)] = item.copy(
//                    quantity = item.quantity?.plus(1) ?: 1
//                )
//            }
//
//        } else {
            lineItems.add(
                InsertingLineItem(
                    properties = listOf(Property(value = puttingCartItem.imageUrl)),
                    variant_id = puttingCartItem.variantId,
                    quantity = puttingCartItem.quantity
                )
            )
//        }

        return lineItems.toList()
    }
}

class AlreadyAddedToCartException : Exception()

fun List<LineItem?>.toListOfPutLineItems(): MutableList<InsertingLineItem> {
    return filterNotNull().map {
        InsertingLineItem(
            properties = it.properties,
            quantity = it.quantity,
            variant_id = it.variant_id,
            price = it.price,
            title = it.title
        )
    }.toMutableList()
}