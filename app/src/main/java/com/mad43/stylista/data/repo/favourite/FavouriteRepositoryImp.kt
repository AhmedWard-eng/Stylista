package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.remote.dataSource.draftOrders.RemoteDraftOrdersDataSource
import com.mad43.stylista.data.remote.dataSource.draftOrders.RemoteDraftOrdersDataSourceImp
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.Customer
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderPostingRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.repo.DraftOrderType
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.Flow

class FavouriteRepositoryImp (private val remoteDraftOrdersDataSource: RemoteDraftOrdersDataSource = RemoteDraftOrdersDataSourceImp()) : FavouriteRepository {
    override suspend fun createFavouriteForCustomer(customerId: Long): RemoteStatus<Long> {
        val draftOrderPostingRequestBodyRequestBody = DraftOrderRequestBody(
            DraftOrderPostingRequestBody(
                Customer(customerId),
                note = DraftOrderType.FAVORITE.type
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

    override suspend fun insertFavouriteForCustomer(idFavourite: Long, draftOrderPutBody: DraftOrderPutBody): RemoteStatus<DraftOrderResponse> {
        return try {

            val draftOrderResponse = remoteDraftOrdersDataSource.putDraftOrder(
                idFavourite,
                draftOrderPutBody = draftOrderPutBody
            )
            RemoteStatus.Success(draftOrderResponse)
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }
    }


    override suspend fun getFavouriteUsingId(id: String):RemoteStatus<CustomDraftOrderResponse> {
        return try {
            val customDraftOrderResponse = remoteDraftOrdersDataSource.getDraftOrderById(id)
            RemoteStatus.Success(customDraftOrderResponse)
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }
    }

}