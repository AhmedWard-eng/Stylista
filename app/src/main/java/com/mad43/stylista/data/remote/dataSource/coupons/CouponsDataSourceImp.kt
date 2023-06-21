package com.mad43.stylista.data.remote.dataSource.coupons

import com.mad43.stylista.data.remote.entity.coupons.DiscountResponse
import com.mad43.stylista.data.remote.entity.coupons.PriceRulesResponse
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.coupons.CouponsApiInterface

class CouponsDataSourceImp(private val couponsApiInterface: CouponsApiInterface = ApiService.couponsApiInterface) : CouponsDataSource {
    override suspend fun getAllPriceRules(): PriceRulesResponse {
        return couponsApiInterface.getAllPriceRules()
    }

    override suspend fun getCouponWithPriceRuleId(priceRuleId: String): DiscountResponse {
        return couponsApiInterface.getCouponWithPriceRuleId(priceRuleId)
    }
}