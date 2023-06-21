package com.mad43.stylista.data.remote.network.coupons

import com.mad43.stylista.data.remote.entity.coupons.DiscountResponse
import com.mad43.stylista.data.remote.entity.coupons.PriceRulesResponse
import com.mad43.stylista.data.remote.entity.orders.ResponseOrders
import com.mad43.stylista.data.remote.entity.orders.post.order.PostOrderResponse
import com.mad43.stylista.util.Constants
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CouponsApiInterface {
        @GET("price_rules.json")
        suspend fun getAllPriceRules(@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD): PriceRulesResponse

        @GET("price_rules/{priceRuleId}/discount_codes.json")
        suspend fun getCouponWithPriceRuleId(@Path("priceRuleId") priceRuleId : String,@Header("X-Shopify-Access-Token") passwordToken: String = Constants.PASSWORD) : DiscountResponse
}