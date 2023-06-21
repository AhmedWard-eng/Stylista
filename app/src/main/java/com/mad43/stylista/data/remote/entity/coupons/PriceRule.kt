package com.mad43.stylista.data.remote.entity.coupons

data class PriceRule(
    val ends_at: String?,
    val id: Long?,
    val starts_at: String?,
    val title: String?,
    val usage_limit: Int?,
    val value: String?,
    val value_type: String?
)