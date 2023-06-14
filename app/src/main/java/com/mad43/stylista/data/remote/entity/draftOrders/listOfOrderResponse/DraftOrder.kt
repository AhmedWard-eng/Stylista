package com.mad43.stylista.data.remote.entity.draftOrders.listOfOrderResponse

import com.mad43.stylista.data.remote.entity.draftOrders.AppliedDiscount
import com.mad43.stylista.data.remote.entity.draftOrders.LineItem
import com.mad43.stylista.data.remote.entity.draftOrders.TaxLine

data class DraftOrder(
    val admin_graphql_api_id: String?,
    val applied_discount: AppliedDiscount?,
    val billing_address: BillingAddress?,
    val completed_at: String?,
    val created_at: String?,
    val currency: String?,
    val customer: Customer?,
    val email: String?,
    val id: Long?,
    val invoice_sent_at: String?,
    val invoice_url: String?,
    val line_items: List<LineItem?>?,
    val name: String?,
    val note: String?,
    val note_attributes: List<Any?>?,
    val order_id: Long?,
    val shipping_address: ShippingAddress?,
    val shipping_line: ShippingLine?,
    val status: String?,
    val subtotal_price: String?,
    val tags: String?,
    val tax_exempt: Boolean?,
    val tax_lines: List<TaxLine?>?,
    val taxes_included: Boolean?,
    val total_price: String?,
    val total_tax: String?,
    val updated_at: String?
)