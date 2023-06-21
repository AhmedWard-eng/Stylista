package com.mad43.stylista.data.repo.coupon

import com.mad43.stylista.data.remote.entity.coupons.DiscountResponse
import com.mad43.stylista.data.remote.entity.coupons.PriceRulesResponse

interface CouponRepo {
    suspend fun getAllPriceRules(): PriceRulesResponse

    suspend fun getCouponWithPriceRuleId(priceRuleId : String) : DiscountResponse
}