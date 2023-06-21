package com.mad43.stylista.data.repo.coupon

import com.mad43.stylista.data.remote.dataSource.coupons.CouponsDataSource
import com.mad43.stylista.data.remote.dataSource.coupons.CouponsDataSourceImp
import com.mad43.stylista.data.remote.entity.coupons.DiscountResponse
import com.mad43.stylista.data.remote.entity.coupons.PriceRulesResponse

class CouponRepoImp(private val couponsDataSource: CouponsDataSource = CouponsDataSourceImp()): CouponRepo {
    override suspend fun getAllPriceRules(): PriceRulesResponse {
        return couponsDataSource.getAllPriceRules()
    }

    override suspend fun getCouponWithPriceRuleId(priceRuleId: String): DiscountResponse {
        return couponsDataSource.getCouponWithPriceRuleId(priceRuleId)
    }
}