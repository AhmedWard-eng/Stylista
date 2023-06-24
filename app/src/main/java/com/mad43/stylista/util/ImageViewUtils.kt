package com.mad43.stylista.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mad43.stylista.R


fun ImageView.setImageFromUrl(url : String){
    Glide.with(context)
        .load(url)
        .apply(
            RequestOptions().override(
                this.width,
                this.height
            )
        )
        .placeholder(R.drawable.place_holder)
        .error(R.drawable.error)
        .into(this)
}