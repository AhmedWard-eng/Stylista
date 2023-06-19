package com.mad43.stylista.ui.address.edit

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
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentAddressesBinding
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.launch

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
            binding.address1.setText(address.address1, TextView.BufferType.EDITABLE)
            binding.address2.setText(address.address2, TextView.BufferType.EDITABLE)
            binding.addressCity.setText(address.city, TextView.BufferType.EDITABLE)
            binding.addressPhone.setText(address.phone, TextView.BufferType.EDITABLE)
            binding.editTextProvince.setText(address.province, TextView.BufferType.EDITABLE)
            binding.editTextCountry.setText(address.country, TextView.BufferType.EDITABLE)
        }

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

    private fun isAllDataCompleted(): Boolean {
        return !(binding.address1.text.isNullOrBlank() ||
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
            address1 = binding.address1.text.toString(),
            address2 = binding.address2.text.toString(),
            city = binding.addressCity.text.toString(),
            phone = binding.addressPhone.text.toString(),
            country = binding.editTextCountry.text.toString(),
            province = binding.editTextProvince.text.toString(),
            isDefault = if (!viewModel.isToEdit) binding.checkBox.isChecked else false

        )
    }
}