package com.mad43.stylista.domain.local.favourite


import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepo
import kotlinx.coroutines.flow.Flow

class FavouriteLocal (val favouriteRepository: FavouriteLocalRepo) {

    suspend fun getStoredProduct(): Flow<List<Favourite>>{
        return favouriteRepository.getStoredProduct()
    }
    suspend fun insertProduct(product: Favourite){
        favouriteRepository.insertProduct(product)
    }
    suspend fun deleteProduct(product: Favourite){
        favouriteRepository.deleteProduct(product)
    }

    fun isProductFavorite(productId: Long): Flow<Boolean> {
        return favouriteRepository.isProductFavorite(productId)
    }

    fun deleteAllProducts() {
        favouriteRepository.deleteAllProducts()
    }

}