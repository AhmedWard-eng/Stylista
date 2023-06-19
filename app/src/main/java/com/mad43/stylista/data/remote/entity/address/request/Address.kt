package com.mad43.stylista.data.remote.entity.address.request

data class Address(
    val address1: String?,
    val address2: String?,
    val city: String?,
    val company: String?,
    val country: String?,
    val default: Boolean?,
    val first_name: String?,
    val last_name: String?,
    val name: String?,
    val phone: String?,
    val province: String?
)