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
    var cityList = listOf("6th of October",
    "Al Sharqia",
    )

    val map = mapOf<String,String>(
        "Alexandria " to "Alexandria",
        "Aswan " to "Aswan",
        "Suez " to "Suez",
        "South Sinai " to "South Sinai",
        "Sohag " to "Sohag",
        "Red Sea " to "Red Sea",
        "Qena " to "Qena",
        "Al Qalyubia " to "Qalyubia",
        "Port Said " to "Port Said",
        "North Sinai " to "North Sinai",
        "New Valley " to "New Valley",
        "Menofia " to "Monufia",
        "Menia " to "Minya",
        "Matrouh " to "Matrouh",
        "Faiyum " to "Faiyum",
        "Gharbia " to "Gharbia",
        "Giza " to "Giza",
        "Ismailia " to "Ismailia",
        "Kafr el-Sheikh " to "Kafr el-Sheikh",
        "Luxor " to "Luxor",
        "Beni Suef " to "Beni Suef",
        "Cairo " to "Cairo",
        "Dakahlia " to "Dakahlia",
        "Damietta " to "Damietta",
        "Aswan " to "Aswan",
        "Assiut " to "Asyut",
        "El Beheira " to "Beheira",
        "Ash Sharqia " to "Al Sharqia"
    )

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