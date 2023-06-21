package com.mad43.stylista.domain.remote

import com.mad43.stylista.data.remote.entity.coupons.DiscountCode
import com.mad43.stylista.data.remote.entity.coupons.PriceRule
import com.mad43.stylista.data.repo.coupon.CouponRepo
import com.mad43.stylista.data.repo.coupon.CouponRepoImp
import com.mad43.stylista.domain.model.CouponItem
import com.mad43.stylista.util.RemoteStatus
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import java.util.Calendar

class GetCouponsListUseCase(private val couponRepo: CouponRepo = CouponRepoImp()) {

    suspend operator fun invoke(): RemoteStatus<List<CouponItem>> {
        val priceRules = couponRepo.getAllPriceRules().price_rules
        val couponItems = mutableListOf<CouponItem>()
        return try {
            if (priceRules != null) {
                priceRules.forEach { priceRule ->
                    couponRepo.getCouponWithPriceRuleId(priceRule?.id.toString()).discount_codes?.forEach { discountCode ->
                        if (discountCode != null && priceRule != null) {
                            couponItems.add(
                                createCouponItemFromPriceRuleAndDiscount(
                                    discountCode,
                                    priceRule
                                )
                            )
                        }
                    }
                }
                RemoteStatus.Success(couponItems.toList())
            } else {
                RemoteStatus.Failure(Exception("null"))
            }
        } catch (e: Exception) {
            RemoteStatus.Failure(e)
        }


    }

    private fun createCouponItemFromPriceRuleAndDiscount(
        discountCode: DiscountCode,
        priceRule: PriceRule
    ): CouponItem {
        return CouponItem(
            isNotExpired = isValid(priceRule.ends_at),
            value = priceRule.value?.toDouble() ?: 0.0,
            code = discountCode.code ?: "",
            price_rule_id = discountCode.price_rule_id ?: 0,
            value_type = priceRule.value_type ?: "",
            id = discountCode.id ?: 0
        )
    }

}

fun isValid(dateString: String?): Boolean {
    return if (dateString == null) {
        true
    } else {
        val date = dateString.split("T")[0]
        val (yearS, monthS, dayS) = date.split("-")

        val year = yearS.toInt()
        val month = monthS.toInt()
        val day = dayS.toInt()

        val calendar = Calendar.getInstance()
        if (year > calendar.get(Calendar.YEAR)) {
            true
        } else if (year == calendar.get(Calendar.YEAR)) {
            if (month > calendar.get(Calendar.MONTH)) {
                true
            } else if (month == calendar.get(Calendar.MONTH)) {
                return day >= calendar.get(Calendar.DAY_OF_MONTH)
            } else false
        } else
            false

    }


    // Convert Instant to Date
}