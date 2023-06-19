package com.mad43.stylista.ui.productInfo.model

import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.product.ProductDetails

sealed class ApiState {

    class Success(val data: ProductDetails) : ApiState()
    class onSuccess(val data: List<Favourite>) : ApiState()
    class Failure(val msg: Throwable) : ApiState()
    object Loading : ApiState()

}