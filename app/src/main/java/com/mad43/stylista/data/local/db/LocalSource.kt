package com.mad43.stylista.data.local.db

import com.mad43.stylista.data.local.entity.Favourite
import kotlinx.coroutines.flow.Flow


interface LocalSource {
    suspend fun insertProduct(product: Favourite)
    suspend fun deleteProduct(product: Favourite)
    suspend fun getStoredProduct(): Flow<List<Favourite>>

    fun isProductFavorite(productId: Long): Flow<Boolean>

    fun deleteAllProducts()
}