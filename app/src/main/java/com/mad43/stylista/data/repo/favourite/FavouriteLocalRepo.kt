package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.local.entity.Favourite
import kotlinx.coroutines.flow.Flow

interface FavouriteLocalRepo {

    suspend fun getStoredProduct(): Flow<List<Favourite>>
    suspend fun insertProduct(product: Favourite)
    suspend fun deleteProduct(product: Favourite)
    fun isProductFavorite(productId: Long): Flow<Boolean>
    fun deleteAllProducts()
}