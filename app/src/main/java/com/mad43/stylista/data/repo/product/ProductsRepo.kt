package com.mad43.stylista.data.repo.product

import android.util.Log
import com.mad43.stylista.data.remote.dataSource.product.RemoteProductsDataSource
import com.mad43.stylista.data.remote.dataSource.product.RemoteProductsDataSourceImp
import com.mad43.stylista.data.remote.entity.brand.mapRemoteBrandToDisplayBrand
import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.data.remote.entity.product.mapRemoteProductToDisplayProduct
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.domain.model.DisplayProduct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class ProductsRepo(private val remoteProductsDataSource: RemoteProductsDataSource = RemoteProductsDataSourceImp()) :
    ProductsRepoInterface {

    override suspend fun getAllBrand(): Flow<List<DisplayBrand>> {
        return try {
            flowOf(remoteProductsDataSource.getAllBrand().smart_collections?.map {
                it.mapRemoteBrandToDisplayBrand()
            }) as Flow<List<DisplayBrand>>
        } catch (e: Exception) {
            flowOf(listOf())
        }

    }

    override suspend fun getAllProductInBrand(brand: String): Flow<List<DisplayProduct>> {
        return try {
            flowOf(remoteProductsDataSource.getAllProductInBrand(brand).products.filter {
                it.vendor == brand
            }.map {
                it.mapRemoteProductToDisplayProduct()
            })
        } catch (e: Exception) {
            flowOf(listOf())
        }

    }

    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> {
        return flowOf(remoteProductsDataSource.getProductById(id))
    }

    override suspend fun getAllProduct(): Flow<List<DisplayProduct>> {
        return try {
            flowOf(remoteProductsDataSource.getAllProduct().products.map { it.mapRemoteProductToDisplayProduct() })
        } catch (e: Exception) {
            flowOf(listOf())
        }
    }

}