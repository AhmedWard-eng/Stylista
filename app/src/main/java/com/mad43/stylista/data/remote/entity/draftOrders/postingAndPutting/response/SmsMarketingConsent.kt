package com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.response

data class SmsMarketingConsent(
    val consent_collected_from: String?,
    val consent_updated_at: String?,
    val opt_in_level: String?,
    val state: String?
)