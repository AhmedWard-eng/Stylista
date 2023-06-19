package com.mad43.stylista.ui.address.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentAddressListBinding
import com.mad43.stylista.domain.model.AddressItem
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.showConfirmationDialog
import kotlinx.coroutines.launch

class AddressListFragment : Fragment() {

    private var _binding: FragmentAddressListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.

    private val binding get() = _binding!!

    private lateinit var adapter : AddressAdapter
    private lateinit var viewModel: AddressListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter = AddressAdapter(AddressAdapter.ClickListener(::editAddress, ::deleteAddress,::setDefault))
        viewModel = ViewModelProvider(this)[AddressListViewModel::class.java]
        viewModel.getAddressList()
        _binding = FragmentAddressListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView2.adapter = adapter
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.addressState.collect{
                    when(it){
                        is RemoteStatus.Success -> {
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.recyclerView2.visibility = View.VISIBLE
                            binding.shimmerFrameLayout.stopShimmerAnimation()
                            adapter.submitList(it.data)
                        }
                        is RemoteStatus.Failure -> {
                            binding.shimmerFrameLayout.visibility = View.GONE
                            binding.shimmerFrameLayout.stopShimmerAnimation()
                        }
                        else -> {
                            binding.shimmerFrameLayout.visibility = View.VISIBLE
                            binding.recyclerView2.visibility = View.GONE
                            binding.shimmerFrameLayout.startShimmerAnimation()
                        }
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.updateState.collect{
                    when(it){
                        is RemoteStatus.Success -> {
                            binding.progressBar.visibility = View.GONE
                            viewModel.getAddressList()
                        }
                        is RemoteStatus.Failure -> {
                            MyDialog().showAlertDialog(getString(R.string.something_went_wrong),requireContext())
                            viewModel.getAddressList()
                            Log.d("TAG", "onViewCreated: ${it.msg}")
                            binding.progressBar.visibility = View.GONE
                        }
                        else -> {
                        }
                    }
                }
            }
        }

        binding.buttonAddNewAddress.setOnClickListener {
            val action = AddressListFragmentDirections.actionAddressListFragmentToAddressDetailsEditFragment(AddressItem(
                customerId = viewModel.customerId ?: -1L
            ))
            Navigation.findNavController(requireView()).navigate(action)
        }


    }


    private fun editAddress(addressItem: AddressItem){
        val action = AddressListFragmentDirections.actionAddressListFragmentToAddressDetailsEditFragment(addressItem)
        Navigation.findNavController(requireView()).navigate(action)
    }

    private fun deleteAddress(addressItem: AddressItem){
        if(addressItem.isDefault){
            MyDialog().showAlertDialog(getString(R.string.cannot_delete_the_customer_s_default_address),requireContext())
        }
        showConfirmationDialog(getString(R.string.are_you_sure_you_want_to_delete_this_address)){
            binding.progressBar.visibility = View.VISIBLE
            viewModel.deleteAddress(addressId = addressItem.addressId.toString())
        }
    }

    private fun setDefault(addressItem: AddressItem){
        showConfirmationDialog(getString(R.string.are_you_sure_you_want_to_make_this_address_your_default_address)){
            binding.progressBar.visibility = View.VISIBLE
            viewModel.setAddressDefault(addressItem.addressId)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}