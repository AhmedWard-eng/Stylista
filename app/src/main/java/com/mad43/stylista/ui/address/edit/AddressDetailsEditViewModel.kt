package com.mad43.stylista.ui.address.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.domain.remote.address.EditAnExistingAddressUseCase
import com.mad43.stylista.domain.remote.address.PostNewAddressUseCase
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressDetailsEditViewModel(
    val editAnExistingAddressUseCase: EditAnExistingAddressUseCase = EditAnExistingAddressUseCase(),
    val postNewAddressUseCase: PostNewAddressUseCase = PostNewAddressUseCase()
) : ViewModel() {
    var isToEdit : Boolean = false

    private val _status = MutableStateFlow<RemoteStatus<Boolean>>(RemoteStatus.Loading)

    val  status = _status.asStateFlow()
    fun editAddress(addressItem: AddressItem){
        viewModelScope.launch {
            _status.emit(editAnExistingAddressUseCase(addressItem))

        }
    }

    fun postAddress(addressItem: AddressItem){
        viewModelScope.launch {
            _status.emit(postNewAddressUseCase(addressItem))
        }
    }
}