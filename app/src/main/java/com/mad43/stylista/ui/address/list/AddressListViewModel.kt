package com.mad43.stylista.ui.address.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.data.sharedPreferences.CustomerManager
import com.mad43.stylista.data.sharedPreferences.PreferencesData
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.remote.address.DeleteAnAddressUseCase
import com.mad43.stylista.domain.remote.address.GetAddressesListUseCase
import com.mad43.stylista.domain.remote.address.SetAddressAsDefaultUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressListViewModel(
    private val getAddressesListUseCase: GetAddressesListUseCase = GetAddressesListUseCase(),
    customerManager: CustomerManager = PreferencesData(),
    private val deleteAnAddressUseCase: DeleteAnAddressUseCase = DeleteAnAddressUseCase(),
    private val setAddressAsDefaultUseCase: SetAddressAsDefaultUseCase = SetAddressAsDefaultUseCase()
) : ViewModel() {

    private val _addressState = MutableStateFlow<RemoteStatus<List<AddressItem>>>(RemoteStatus.Loading)

    private val _updateState = MutableStateFlow<RemoteStatus<Boolean>>(RemoteStatus.Loading)


    val updateState = _updateState.asStateFlow()

    val addressState = _addressState.asStateFlow()


    var customerId : Long? = null
    var chooseAddress = false
    val result = customerManager.getCustomerData()
    init {
        if(result.isSuccess){
            customerId = result.getOrNull()?.customerId
        }
    }

    fun deleteAddress(addressId : String){
        viewModelScope.launch {
            _updateState.emit(deleteAnAddressUseCase(customerId.toString(),addressId))
        }
    }


    fun isUserLoggedIn() : Boolean{
        return result.isSuccess
    }
    fun getAddressList(){
        viewModelScope.launch {
            _addressState.emit(
                getAddressesListUseCase(customerId.toString()))
        }
    }

    fun setAddressDefault(addressId: Long){
        viewModelScope.launch {
            _updateState.emit(setAddressAsDefaultUseCase(customerId.toString(),addressId.toString()))
        }
    }
}