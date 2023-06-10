package com.mad43.stylista.data.remote.entity.product

data class Image(
    val id: Long,
    val product_id: Long,
    val position: Int,
    val created_at: String,
    val updated_at: String,
    val alt: Any,
    val width: Int,
    val height: Int,
    val src: String,
    val variant_ids: List<Any>,
    val admin_graphql_api_id: String,
)
