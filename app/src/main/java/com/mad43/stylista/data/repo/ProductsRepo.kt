package com.mad43.stylista.data.repo

import com.mad43.stylista.data.remote.dataSource.RemoteProductsDataSource
import com.mad43.stylista.data.remote.dataSource.RemoteProductsDataSourceImp
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

        return flowOf(remoteProductsDataSource.getAllBrand().smart_collections?.map {
            it.mapRemoteBrandToDisplayBrand()
        }) as Flow<List<DisplayBrand>>

    }

    override suspend fun getProductDetails(id: Long): Flow<ProductDetails> {
        return flowOf(remoteProductsDataSource.getProductDetails(id))
    }

    override suspend fun getAllProductInBrand(brand: String): Flow<List<DisplayProduct>> {
        return flowOf(remoteProductsDataSource.getAllProductInBrand(brand).products.filter {
            it.vendor == brand
        }.map { it.mapRemoteProductToDisplayProduct() })
    }

}