package com.mad43.stylista.data.remote.entity.draftOrders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Property(
    val name: String? =  "url_image",
    val value: String?
) : Parcelable