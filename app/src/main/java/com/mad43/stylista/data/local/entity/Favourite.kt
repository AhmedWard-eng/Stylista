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
)

//class VariantTypeConverter {
//    @TypeConverter
//    fun fromStringVariant(value: String): List<Variant> {
//        val listType = object : TypeToken<List<Variant>>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromListVariant(list: List<Variant>): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//
//    @TypeConverter
//    fun fromStringOption(value: String): List<Option> {
//        val listType = object : TypeToken<List<Option>>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//    @TypeConverter
//    fun fromListOption(list: List<Option>): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//    @TypeConverter
//    fun fromStringImages(value: String): List<Images> {
//        val listType = object : TypeToken<List<Images>>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromListImages(list: List<Images>): String {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//    @TypeConverter
//    fun fromStringImage(value: String): Image {
//        return Gson().fromJson(value, Image::class.java)
//    }
//
//    @TypeConverter
//    fun fromImage(image: Image): String {
//        val gson = Gson()
//        return gson.toJson(image)
//    }
//}