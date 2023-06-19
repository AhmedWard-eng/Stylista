package com.mad43.stylista.data.local.db

import android.content.Context
import com.mad43.stylista.data.local.entity.Favourite
import kotlinx.coroutines.flow.Flow

class ConcreteLocalSource(context: Context) :LocalSource{

    private val dao:ProductDao by lazy {
        val db: ProductDataBase = ProductDataBase.getInstance(context)
        db.getProductDao()
    }
    override suspend fun insertProduct(product: Favourite) {
        dao.insert(product)
    }

    override suspend fun deleteProduct(product: Favourite) {
        dao.delete(product)
    }

    override suspend fun getStoredProduct(): Flow<List<Favourite>> {
        return dao.getAll()
    }

    override fun isProductFavorite(productId: Long): Flow<Boolean> {
        return dao.isProductFavorite(productId)
    }
    override fun deleteAllProducts() {
        dao.deleteAll()

    }
}