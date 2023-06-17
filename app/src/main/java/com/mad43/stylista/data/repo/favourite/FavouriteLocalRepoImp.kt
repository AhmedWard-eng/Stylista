package com.mad43.stylista.data.repo.favourite

import com.mad43.stylista.data.local.db.LocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import com.mad43.stylista.data.sharedPreferences.LocalCustomer
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import kotlinx.coroutines.flow.Flow

class FavouriteLocalRepoImp (var localSource: LocalSource , private val userManager: CustomerManager = PreferencesData()) : FavouriteLocalRepo {
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
    override fun getCustomerData(): Result<LocalCustomer> {
        return userManager.getCustomerData()
    }
}