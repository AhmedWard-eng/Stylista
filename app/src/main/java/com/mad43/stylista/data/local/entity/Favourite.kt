package com.mad43.stylista.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.mad43.stylista.data.remote.entity.product.Image
import com.mad43.stylista.data.remote.entity.product.Images
import com.mad43.stylista.data.remote.entity.product.Option
import com.mad43.stylista.data.remote.entity.product.Variant

@Entity(tableName = "Product")
data class Favourite(
    @PrimaryKey
    val id: Long,
    val title: String,
    val price : String,
    val image: String,
    val variantID : Long
)
