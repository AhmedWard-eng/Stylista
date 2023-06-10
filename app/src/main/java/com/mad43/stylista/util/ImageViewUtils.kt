package com.mad43.stylista.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


fun ImageView.setImageFromUrl(url : String){
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions().override(
                this.width,
                this.height
            )
        )
        .into(this)
}