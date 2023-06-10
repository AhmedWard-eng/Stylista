package com.mad43.stylista.domain.model

data class DisplayProduct(
    val id: Long,
    val price: String,
    val product_type: String,
    val title: String,
    val image: String,
    val tag : String
)
