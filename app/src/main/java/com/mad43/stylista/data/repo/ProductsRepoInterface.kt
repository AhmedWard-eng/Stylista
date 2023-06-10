package com.mad43.stylista.data.repo

import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.domain.model.DisplayProduct
import kotlinx.coroutines.flow.Flow

interface ProductsRepoInterface {
    suspend fun getAllBrand() : Flow<List<DisplayBrand>>
    suspend fun getAllProductInBrand(brand: String) : Flow<List<DisplayProduct>>
    suspend fun getAllProducts() : Flow<List<DisplayProduct>>
}