package com.mad43.stylista.domain.model

data class CouponItem(
    val isNotExpired: Boolean,
    val value: Double,
    val value_type: String,
    val code: String,
    val id: Long,
    val price_rule_id: Long,
)