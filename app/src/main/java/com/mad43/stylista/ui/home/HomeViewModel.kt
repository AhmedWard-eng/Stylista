package com.mad43.stylista.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.data.repo.product.ProductsRepoInterface
import com.mad43.stylista.domain.model.CouponItem
import com.mad43.stylista.domain.remote.GetCouponsListUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(
    private val repoInterface: ProductsRepoInterface = ProductsRepo(),
    private val getCouponsListUseCase: GetCouponsListUseCase = GetCouponsListUseCase()
) :
    ViewModel() {
    val ads = ArrayList<SlideModel>()
    var brands = MutableStateFlow<RemoteStatus<List<DisplayBrand>>>(RemoteStatus.Loading)
    private val _couponState = MutableStateFlow<RemoteStatus<CouponItem>>(RemoteStatus.Loading)
    val couponState = _couponState.asStateFlow()

    var couponCode = ""

    init {
        ads.add(SlideModel("https://i.pinimg.com/564x/10/af/c9/10afc9456a581c807712bd778c71b36e.jpg"))
        ads.add(SlideModel("https://st4.depositphotos.com/1001941/30071/v/1600/depositphotos_300718676-stock-illustration-creative-sale-poster-or-template.jpg"))
        ads.add(SlideModel("https://i.pinimg.com/564x/2b/5e/66/2b5e6631ef74bdd017b2db9cdc2c0abb.jpg"))
        ads.add(SlideModel("https://i.pinimg.com/564x/ae/29/88/ae29881c12bfc6edc409794a29c39743.jpg"))
        ads.add(SlideModel("https://i.pinimg.com/564x/32/b4/e1/32b4e1726924c26ceaba6ae906cfcdd2.jpg"))

        getBrand()
    }

    fun getBrand() {
        viewModelScope.launch {
            try {
                repoInterface.getAllBrand().catch { e ->
                    brands.value = RemoteStatus.Failure(e)
                }.collect { data ->
                    brands.value = RemoteStatus.Success(data)
                }
            } catch (e: Exception) {
                brands.value = RemoteStatus.Failure(e)
            }

        }

    }

    fun getRandomCoupon() {
        viewModelScope.launch {
            when (val status = getCouponsListUseCase()) {
                is RemoteStatus.Success -> {
                    if (status.data.isNotEmpty())
                        _couponState.emit(RemoteStatus.Success(status.data.random()))

                }

                is RemoteStatus.Failure -> {
                    _couponState.emit(status)
                }

                else -> {

                }
            }

        }

    }
}