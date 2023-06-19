package com.mad43.stylista.ui.address.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentAddressListBinding
import com.mad43.stylista.databinding.FragmentCartBinding
import com.mad43.stylista.domain.model.AddressItem

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
        adapter = AddressAdapter(AddressAdapter.ClickListener(::editAddress, ::deleteAddress))
        viewModel = ViewModelProvider(this)[AddressListViewModel::class.java]

        _binding = FragmentAddressListBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun editAddress(addressItem: AddressItem){

    }

    private fun deleteAddress(addressItem: AddressItem){

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}