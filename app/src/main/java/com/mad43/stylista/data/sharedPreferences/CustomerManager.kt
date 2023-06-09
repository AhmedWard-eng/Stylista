package com.mad43.stylista.data.sharedPreferences

interface CustomerManager {
    fun saveCustomerData(localCustomer: LocalCustomer)
    fun getCustomerData() : Result<LocalCustomer>
    fun removeCustomerData()
}