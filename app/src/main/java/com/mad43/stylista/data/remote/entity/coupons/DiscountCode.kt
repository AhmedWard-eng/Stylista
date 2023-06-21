package com.mad43.stylista.data.remote.entity.coupons

data class DiscountCode(
    val code: String?,
    val created_at: String?,
    val id: Long?,
    val price_rule_id: Long?,
    val updated_at: String?,
    val usage_count: Int?
)