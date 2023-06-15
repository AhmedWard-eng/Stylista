package com.mad43.stylista.data.sharedPreferences

import android.content.SharedPreferences
import com.mad43.stylista.util.Constants.Companion.CARD_ID
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_EMAIL
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_ID
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_STATE
import com.mad43.stylista.util.Constants.Companion.FAVOURITE_ID
import com.mad43.stylista.util.Constants.Companion.USER_NAME

class PreferencesData(private val sharedPreferences: SharedPreferences = SharedPref.sharedPreferences) :
    CustomerManager {
    private val editor = sharedPreferences.edit()
    override fun saveCustomerData(localCustomer: LocalCustomer) {
        editor?.putLong(CUSTOMER_ID, localCustomer.customerId)
        editor?.putBoolean(CUSTOMER_STATE, localCustomer.state)
        editor?.putString(CUSTOMER_EMAIL, localCustomer.email)
        editor?.putString(USER_NAME, localCustomer.userName)
        editor?.putString(CARD_ID, localCustomer.cardID)
        editor?.putString(FAVOURITE_ID, localCustomer.favouriteID)
        editor?.apply()
    }

    override fun getCustomerData(): Result<LocalCustomer> {
        val customerId = sharedPreferences.getLong(CUSTOMER_ID, 0)
        val state = sharedPreferences.getBoolean(CUSTOMER_STATE, false)
        val email = sharedPreferences.getString(CUSTOMER_EMAIL, "")
        val userName = sharedPreferences.getString(USER_NAME, "")
        val cardID = sharedPreferences.getString(CARD_ID, "")
        val favouriteID = sharedPreferences.getString(FAVOURITE_ID, "")
        return if ((customerId == 0L) || state == false || email == null || userName == null || cardID == null || favouriteID == null) {
            Result.failure(Exception("User not Found"))
        } else {
            Result.success(
                LocalCustomer(
                    customerId = customerId ,
                    state = state,
                    email = email,
                    userName = userName.toString(),
                    cardID = cardID.toString(),
                    favouriteID = favouriteID.toString()
                )
            )
        }
    }

    override fun removeCustomerData() {
        editor?.putLong(CUSTOMER_ID, 0L)
        editor?.putBoolean(CUSTOMER_STATE, false)
        editor?.putString(CUSTOMER_EMAIL, null)
        editor?.putString(USER_NAME, null)
        editor?.putString(CARD_ID, null)
        editor?.putString(FAVOURITE_ID, null)
        editor?.apply()
    }
}