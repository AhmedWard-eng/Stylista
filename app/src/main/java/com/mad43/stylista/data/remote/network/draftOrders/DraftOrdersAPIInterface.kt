package com.mad43.stylista.data.remote.network.draftOrders

import com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse.AllDraftOrders
import com.mad43.stylista.data.remote.entity.draftOrders.oneOrderResponse.CustomDraftOrderResponse
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.postingrequestBody.DraftOrderRequestBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.puttingrequestBody.DraftOrderPutBody
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response.DraftOrderResponse
import com.mad43.stylista.util.Constants
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface DraftOrdersAPIInterface {
    @POST("draft_orders.json")
    suspend fun postDraftOrder(@Body draftOrderRequestBody: DraftOrderRequestBody, @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD) : DraftOrderResponse

    @PUT("draft_orders/{id}.json")
    suspend fun putDraftOrder(@Path("id") id : String, @Body draftOrderPutBody: DraftOrderPutBody, @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD) : DraftOrderResponse

    @GET("draft_orders.json")
    suspend fun getAllDraftOrders(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD) : AllDraftOrders

    @GET("draft_orders/{id}.json")
    suspend fun getDraftOrderById(@Path("id") id : String, @Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD) : CustomDraftOrderResponse

}