package com.mad43.stylista.data.local.db

import androidx.room.*
import com.mad43.stylista.data.local.entity.Favourite
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Query("SELECT * FROM Product")
    fun getAll(): Flow<List<Favourite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(product: Favourite)

    @Delete
    fun delete(product: Favourite)

    @Update
    fun update(product: Favourite)

    @Query("SELECT EXISTS(SELECT 1 FROM Product WHERE id = :productId)")
    fun isProductFavorite(productId: Long): Flow<Boolean>

    @Query("DELETE FROM Product")
    fun deleteAll()
}