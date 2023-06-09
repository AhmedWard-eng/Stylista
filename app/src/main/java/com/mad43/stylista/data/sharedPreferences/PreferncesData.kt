package com.mad43.stylista.data.sharedPreferences

import android.content.SharedPreferences
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_EMAIL
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_ID
import com.mad43.stylista.util.Constants.Companion.CUSTOMER_STATE
import com.mad43.stylista.util.Constants.Companion.USER_IDFIRBASE

class PreferencesData(private val sharedPreferences: SharedPreferences = SharedPref.sharedPreferences) :
    CustomerManager {
    private val editor = sharedPreferences.edit()
    override fun saveCustomerData(localCustomer: LocalCustomer) {
        editor?.putLong(CUSTOMER_ID, localCustomer.customerId)
        editor?.putBoolean(CUSTOMER_STATE, localCustomer.state)
        editor?.putString(CUSTOMER_EMAIL, localCustomer.email)
        editor?.putString(USER_IDFIRBASE, localCustomer.userIdFirBase)
        editor?.apply()
    }

    override fun getCustomerData(): Result<LocalCustomer> {
        val customerId = sharedPreferences.getLong(CUSTOMER_ID, 0)
        val state = sharedPreferences.getBoolean(CUSTOMER_STATE, false)
        val email = sharedPreferences.getString(CUSTOMER_EMAIL, "")
        val userIdFirBase = sharedPreferences.getString(USER_IDFIRBASE, "")
        return if ((customerId == 0L) || state == false || email == null || userIdFirBase == null) {
            Result.failure(Exception("User not Found"))
        } else {
            Result.success(
                LocalCustomer(
                    customerId = customerId ,
                    state = state,
                    email = email,
                    userIdFirBase = userIdFirBase.toString()
                )
            )
        }
    }

    override fun removeCustomerData() {
        editor?.putLong(CUSTOMER_ID, 0L)
        editor?.putBoolean(CUSTOMER_STATE, false)
        editor?.putString(CUSTOMER_EMAIL, null)
        editor?.putString(USER_IDFIRBASE, null)
        editor?.apply()
    }
}