package com.mad43.stylista.data.remote.dataSource.draftOrders

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.AllDraftOrders
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.draftOrders.DraftOrdersAPIInterface
import com.mad43.stylista.data.remote.network.product.ProductsAPIInterface

class RemoteDraftOrdersDataSourceImp(private val apiInterface: DraftOrdersAPIInterface = ApiService.draftAPIService,private val productsApi : ProductsAPIInterface = ApiService.brandsApiService ) : RemoteDraftOrdersDataSource {
    override suspend fun postDraftOrder(draftOrderRequestBody: DraftOrderRequestBody): DraftOrderResponse {
        return apiInterface.postDraftOrder(draftOrderRequestBody)
    }

    override suspend fun putDraftOrder(id: Long, draftOrderPutBody: DraftOrderPutBody): DraftOrderResponse {
        return apiInterface.putDraftOrder(id.toString(),draftOrderPutBody)
    }

    override suspend fun getAllDraftOrders(): AllDraftOrders {
        return apiInterface.getAllDraftOrders()
    }

    override suspend fun getDraftOrderById(id: String): CustomDraftOrderResponse {
        return apiInterface.getDraftOrderById(id)
    }

    override suspend fun getAllProduct(): List<Product> {
        return productsApi.getAllProducts().products
    }
}