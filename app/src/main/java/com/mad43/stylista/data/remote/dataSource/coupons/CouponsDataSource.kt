package com.mad43.stylista.data.remote.dataSource.coupons

import com.mad43.stylista.data.remote.entity.coupons.DiscountResponse
import com.mad43.stylista.data.remote.entity.coupons.PriceRulesResponse
import com.mad43.stylista.util.Constants
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface CouponsDataSource {

    suspend fun getAllPriceRules(): PriceRulesResponse

    suspend fun getCouponWithPriceRuleId(priceRuleId : String) : DiscountResponse

}