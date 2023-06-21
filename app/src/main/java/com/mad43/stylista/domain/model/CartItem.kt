package com.mad43.stylista.domain.model

import android.os.Parcelable
import com.mad43.stylista.data.remote.entity.draftOrders.LineItem
import com.mad43.stylista.data.remote.entity.draftOrders.Property
import com.mad43.stylista.data.remote.entity.draftOrders.TaxLine
import com.mad43.stylista.data.remote.entity.draftOrders.postingAndPutting.InsertingLineItem
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    val id: Long,
    val name: String,
    val price: Double,
    val product_id: Long,
    val imageUrl: String,
    val quantity: Int,
    val requires_shipping: Boolean,
    val taxes: Double,
    val taxable: Boolean,
    val title: String,
    val variant_id: Long,
    val sku: String,
    val variant_title: String,
) : Parcelable

fun List<CartItem>.toInsertingLineItemList(): List<InsertingLineItem> {
    return map { cartItem ->
        InsertingLineItem(
            properties = listOf(Property(value = cartItem.imageUrl)),
            variant_id = cartItem.variant_id,
            quantity = cartItem.quantity
        )
    }
}

fun List<LineItem?>.toCartItemList(): List<CartItem> {
    return filterNotNull().filter { it.variant_id != null }.map { lineItem ->
        CartItem(
            id = lineItem.id ?: 0,
            name = lineItem.name ?: "",
            price = lineItem.price?.toDouble() ?: 0.0,
            product_id = lineItem.product_id ?: 0L,
            imageUrl = lineItem.properties?.find { property -> property.name == "url_image" }?.value
                ?: "",
            quantity = lineItem.quantity ?: 0,
            requires_shipping = lineItem.requires_shipping ?: false,
            taxes = getTaxesFromList(lineItem.tax_lines),
            taxable = lineItem.taxable ?: false,
            title = lineItem.title ?: "",
            variant_id = lineItem.variant_id ?: 0L,
            sku = lineItem.sku ?: "",
            variant_title = lineItem.variant_title ?: ""
        )

    }
}

fun getTaxesFromList(taxLines: List<TaxLine>?): Double {
    return if (taxLines.isNullOrEmpty()) 0.0
    else {
        var taxes = 0.0
        for (tax in taxLines) {
            taxes += tax.price?.toDouble() ?: 0.0
        }
        taxes
    }
}
