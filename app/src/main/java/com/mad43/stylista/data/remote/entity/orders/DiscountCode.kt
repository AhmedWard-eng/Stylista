package com.mad43.stylista.data.remote.entity.orders

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountCode(val code: String?, val amount: String?, val type: String?) : Parcelable
