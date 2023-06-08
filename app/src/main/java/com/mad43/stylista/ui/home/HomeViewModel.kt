package com.mad43.stylista.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.denzcoskun.imageslider.models.SlideModel

class HomeViewModel : ViewModel() {
    val ads = ArrayList<SlideModel>()

    init{
        ads.add(SlideModel("https://picsum.photos/seed/picsum/200/300"))
        ads.add(SlideModel("https://picsum.photos/200/300"))
        ads.add(SlideModel("https://picsum.photos/id/237/200/300"))
        ads.add(SlideModel("https://picsum.photos/200"))
        ads.add(SlideModel("https://picsum.photos/200/300/?blur"))
        ads.add(SlideModel("https://picsum.photos/200/300.jpg"))
    }
}