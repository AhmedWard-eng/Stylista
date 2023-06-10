package com.mad43.stylista.data.remote.entity.auth

data class Customer(
    val email: String,
    val email_marketing_consent: EmailMarketingConsent,
    val first_name: String,
    val id: Long,
    val last_name: String,
    val last_order_id: Int,
    val last_order_name: String,
    val marketing_opt_in_level: Any,
    val multipass_identifier: Any,
    val note: String ="",
    val orders_count: Long,
    val phone: String,
    val sms_marketing_consent: SmsMarketingConsent,
    val state: String,
    val tags: String,
    val tax_exempt: Boolean,
    val tax_exemptions: List<Any>,
    val total_spent: String,
    val updated_at: String,
    val verified_email: Boolean,
   // val password:String,
   // val password_confirmation : String
)