package com.mad43.stylista.util

import com.mad43.stylista.BuildConfig


private const val PASSWORD_KEY = BuildConfig.PASSWORD
class Constants {

    companion object {
        const val PASSWORD = PASSWORD_KEY
        const val BASE_URL = "https://mad43-sv-and0.myshopify.com/admin/api/2023-04/"
        const val CUSTOMER_PREF = "CUSTOMER_PREF"
        const val CUSTOMER_ID = "CUSTOMER_ID"
        const val CUSTOMER_EMAIL = "CUSTOMER_EMAIL"
        const val CUSTOMER_STATE = "CUSTOMER_STATE"
        const val USER_NAME = "USER_NAME"
        const val CARD_ID = "CARD_ID"
        const val FAVOURITE_ID = "FAVOURITE_ID"
    }
}
