package com.mad43.stylista.domain.model

data class PuttingCartItem(
    val cartId: Long,
    val variantId: Long,
    val quantity: Int,
    val userEmail: String,
    val imageUrl : String
)