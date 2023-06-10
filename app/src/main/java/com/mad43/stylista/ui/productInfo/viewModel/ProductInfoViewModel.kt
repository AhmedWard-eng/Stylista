package com.mad43.stylista.ui.productInfo.viewModel

import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel

class ProductInfoViewModel : ViewModel() {

    val testArray = ArrayList<SlideModel>()

    init{
        testArray.add(SlideModel("https://picsum.photos/seed/picsum/200/300"))
        testArray.add(SlideModel("https://picsum.photos/200/300"))
        testArray.add(SlideModel("https://picsum.photos/id/237/200/300"))
        testArray.add(SlideModel("https://picsum.photos/200"))
        testArray.add(SlideModel("https://picsum.photos/200/300/?blur"))
        testArray.add(SlideModel("https://picsum.photos/200/300.jpg"))
    }
}