package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.local.db.LocalSource
import com.mad43.stylista.data.local.entity.Favourite
import kotlinx.coroutines.flow.Flow

class FavouriteLocalRepoImp (var localSource: LocalSource) : FavouriteLocalRepo {
    override suspend fun getStoredProduct(): Flow<List<Favourite>> {
        return localSource.getStoredProduct()
    }

    override suspend fun insertProduct(product: Favourite) {
        return localSource.insertProduct(product)
    }

    override suspend fun deleteProduct(product: Favourite) {
        return localSource.deleteProduct(product)
    }
    override fun isProductFavorite(productId: Long): Flow<Boolean> {
        return localSource.isProductFavorite(productId)
    }
    override fun deleteAllProducts() {
        localSource.deleteAllProducts()
    }
}