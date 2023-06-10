package com.mad43.stylista.data.remote.entity.product

data class Option(
    val id: Long,
    val product_id: Long,
    val name: String,
    val position: Int,
    val values: List<String>,
)
