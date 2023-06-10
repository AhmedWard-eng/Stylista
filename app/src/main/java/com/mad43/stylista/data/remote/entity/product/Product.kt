package com.mad43.stylista.data.remote.entity.product

import com.mad43.stylista.domain.model.DisplayProduct

data class Product(
    val id: Long,
    val title: String,
    val body_html: String,
    val vendor: String,
    val product_type: String,
    val created_at: String,
    val handle: String,
    val updated_at: String,
    val published_at: String,
    val template_suffix: Any,
    val status: String,
    val published_scope: String,
    val tags: String,
    val admin_graphql_api_id: String,
    val variants: List<Variant>,
    val options: List<Option>,
    val images: List<Images>,
    val image: Image,
)

fun Product.mapRemoteProductToDisplayProduct(): DisplayProduct {
    return DisplayProduct(this.id,this.variants[0].price, this.product_type,this.title ?: "", image?.src ?: "")
}

