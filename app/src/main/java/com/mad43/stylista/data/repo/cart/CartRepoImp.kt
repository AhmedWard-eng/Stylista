package com.mad43.stylista.data.repo.cart

import com.mad43.stylista.data.remote.dataSource.draftOrders.RemoteDraftOrdersDataSource
import com.mad43.stylista.data.remote.dataSource.draftOrders.RemoteDraftOrdersDataSourceImp
import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.DraftOrder
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.Customer
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderPostingRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.repo.DraftOrderType
import com.mad43.stylista.util.RemoteStatus

class CartRepoImp(private val remoteDraftOrdersDataSource: RemoteDraftOrdersDataSource = RemoteDraftOrdersDataSourceImp()) : CartRepo {


    override suspend fun createCartForCustomer(customerId: Long): RemoteStatus<Long> {
        val draftOrderPostingRequestBodyRequestBody = DraftOrderRequestBody(
            DraftOrderPostingRequestBody(
                Customer(customerId),
                note = DraftOrderType.CART.type
            )
        )

        return try {
            val draftOrderResponse =
                remoteDraftOrdersDataSource.postDraftOrder(draftOrderPostingRequestBodyRequestBody)
            val draftOrderId = draftOrderResponse.draft_order?.id
            if (draftOrderId != null) {
                RemoteStatus.Success(draftOrderId)
            } else {
                RemoteStatus.Failure(Exception("The response is null"))
            }
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }


    }

    override suspend fun updateCart(
        id: Long,
        draftOrderPutBody: DraftOrderPutBody
    ): RemoteStatus<DraftOrderResponse> {

        return try {

            val draftOrderResponse = remoteDraftOrdersDataSource.putDraftOrder(
                id,
                draftOrderPutBody = draftOrderPutBody
            )
            RemoteStatus.Success(draftOrderResponse)
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }


    }


    override suspend fun getCartUsingId(id: String): RemoteStatus<CustomDraftOrderResponse> {
        return try {
            val customDraftOrderResponse = remoteDraftOrdersDataSource.getDraftOrderById(
                id
            )
            RemoteStatus.Success(customDraftOrderResponse)
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }
    }

    override suspend fun getCartWithEmail(email : String):
            RemoteStatus<DraftOrder> {
        return try {
            val cart =
                remoteDraftOrdersDataSource.getAllDraftOrders().draft_orders?.filter {
                    it?.note == DraftOrderType.CART.type
                }?.find{it?.email == email}
            if (cart != null)
                RemoteStatus.Success(cart)
            else RemoteStatus.Failure(Exception("No Cart Yet"))
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }
    }





}