package com.mad43.stylista.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denzcoskun.imageslider.models.SlideModel
import com.mad43.stylista.data.repo.product.ProductsRepo
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.data.repo.product.ProductsRepoInterface
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel(private val repoInterface: ProductsRepoInterface = ProductsRepo()) :
    ViewModel() {
    val ads = ArrayList<SlideModel>()
    var brands = MutableStateFlow<RemoteStatus<List<DisplayBrand>>>(RemoteStatus.Loading)

    init {
        ads.add(SlideModel("https://picsum.photos/seed/picsum/200/300"))
        ads.add(SlideModel("https://picsum.photos/200/300"))
        ads.add(SlideModel("https://picsum.photos/id/237/200/300"))
        ads.add(SlideModel("https://picsum.photos/200"))
        ads.add(SlideModel("https://picsum.photos/200/300/?blur"))
        ads.add(SlideModel("https://picsum.photos/200/300.jpg"))

        getBrand()
    }

    private fun getBrand() {
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
}