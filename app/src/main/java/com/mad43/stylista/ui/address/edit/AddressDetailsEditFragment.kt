package com.mad43.stylista.ui.address.edit

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentAddressesBinding
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.util.Constants
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.launch
import java.util.Locale

class AddressDetailsEditFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!
    private lateinit var viewModel: AddressDetailsEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[AddressDetailsEditViewModel::class.java]
        _binding = FragmentAddressesBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val address = AddressDetailsEditFragmentArgs.fromBundle(requireArguments()).address
        if (address.addressId != -1L) {
            viewModel.isToEdit = true
            binding.checkBox.visibility = GONE
            binding.addressTitle.setText(address.address1, TextView.BufferType.EDITABLE)
            binding.address2.setText(address.address2, TextView.BufferType.EDITABLE)
            binding.addressCity.setText(address.city, TextView.BufferType.EDITABLE)
            binding.addressPhone.setText(address.phone, TextView.BufferType.EDITABLE)
            binding.editTextProvince.setText(address.province, TextView.BufferType.EDITABLE)
            binding.editTextCountry.setText(address.country, TextView.BufferType.EDITABLE)
        }

        Places.initialize(requireContext().applicationContext,Constants.GOOGLE_KEY)
        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                place.address
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: ${place.address}")
                val latLng = place.latLng
                if( latLng != null){
                    getAddress(requireContext(), latLng,::fillEditText,::showError)
                }else{
                    Log.d("TAG", "onPlaceSelected: $latLng")
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: $status")
            }
        })

        binding.button.setOnClickListener {
            if (isAllDataCompleted()) {
                if(viewModel.isToEdit){
                    viewModel.editAddress(getItemAddressFromEditText(address.customerId,address))
                }else{
                    viewModel.postAddress(getItemAddressFromEditText(address.customerId, address))
                }
            }else{
                MyDialog().showAlertDialog(getString(R.string.please_fill_all_data),requireContext())
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.status.collect{
                    when(it) {
                        is RemoteStatus.Success -> {
                            binding.progressBar.visibility = GONE
                            Navigation.findNavController(requireView()).navigateUp()
                        }

                        is RemoteStatus.Failure -> {
                            MyDialog().showAlertDialog(
                                getString(R.string.something_went_wrong),
                                requireContext()
                            )
                            Log.d("TAG", "onViewCreated: ${it.msg}")
                            binding.progressBar.visibility = GONE
                        }

                        else -> {
                        }
                    }
                }
            }
        }

    }



    private fun showError(e: Exception) {

    }

    private fun fillEditText(address: Address?) {

        val provinceList = address?.adminArea?.trim()?.split("Gov")

        Log.d("TAG", "fillEditText: $provinceList")
        if (!provinceList.isNullOrEmpty()){
            var governance = getGovernance(provinceList[0])
            binding.addressCity.setText(address.subAdminArea, TextView.BufferType.EDITABLE)
//        binding.addressPhone.setText(address.phone, TextView.BufferType.EDITABLE)
            binding.editTextProvince.setText(governance, TextView.BufferType.EDITABLE)
            binding.editTextCountry.setText(address.countryName, TextView.BufferType.EDITABLE)
        }

//        binding.addressTitle.setText(address., TextView.BufferType.EDITABLE)
//        binding.address2.setText(address.address2, TextView.BufferType.EDITABLE)

    }

    private fun getGovernance(governance: String): String {
        val go = viewModel.map[governance]
        return go ?: governance
    }

    private fun isAllDataCompleted(): Boolean {
        return !(binding.addressTitle.text.isNullOrBlank() ||
                binding.address2.text.isNullOrBlank() ||
                binding.addressCity.text.isNullOrBlank() ||
                binding.addressPhone.text.isNullOrBlank() ||
                binding.editTextProvince.text.isNullOrBlank() ||
                binding.editTextCountry.text.isNullOrBlank())
    }

    private fun getItemAddressFromEditText(customerId: Long, address: AddressItem): AddressItem{
        return AddressItem(
            customerId = customerId,
            addressId = address.addressId,
            address1 = binding.addressTitle.text.toString(),
            address2 = binding.address2.text.toString(),
            city = binding.addressCity.text.toString(),
            phone = binding.addressPhone.text.toString(),
            country = binding.editTextCountry.text.toString(),
            province = binding.editTextProvince.text.toString(),
            isDefault = if (!viewModel.isToEdit) binding.checkBox.isChecked else false

        )
    }
}

fun getAddress(
    context: Context, latLng: com.google.android.gms.maps.model.LatLng, onResult: (Address?) -> Unit,
    onError:(Exception)-> Unit) {

    try {
        var address: Address?
        val geocoder = Geocoder(context,Locale.US)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                    latLng.latitude, latLng.longitude, 1
                ) {
                    address = it[0]
                    onResult(address)
                }
        } else {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.get(0)
            onResult(address)
        }
    }catch (e: Exception){
        onError(e)
    }

}