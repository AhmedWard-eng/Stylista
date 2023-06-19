package com.mad43.stylista.ui.address.edit

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mad43.stylista.R

class AddressDetailsEditFragment : Fragment() {

    private lateinit var viewModel: AddressDetailsEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(this)[AddressDetailsEditViewModel::class.java]
        return inflater.inflate(R.layout.fragment_addresses, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}