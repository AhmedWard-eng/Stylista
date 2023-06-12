package com.mad43.stylista.data.remote.dataSource

import com.mad43.stylista.data.remote.entity.brand.BrandResponse
import com.mad43.stylista.data.remote.entity.product.Product
import com.mad43.stylista.data.remote.entity.product.ProductDetails
import com.mad43.stylista.data.remote.entity.product.ProductResponse
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.remote.network.ProductsAPIInterface

class RemoteProductsDataSourceImp(private val productsAPIInterface: ProductsAPIInterface = ApiService.brandsApiService) :
    RemoteProductsDataSource {
    override suspend fun getAllBrand(): BrandResponse {
        return productsAPIInterface.getAllBrand()
    }

    override suspend fun getAllProductInBrand(brand: String): ProductResponse {
        return productsAPIInterface.getAllProducts()
    }

    override suspend fun getProductById(id: Long): ProductDetails {
        return productsAPIInterface.getProductDetails(id= id)
    }


}