package com.mad43.stylista.ui.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentCategoryBinding
import com.mad43.stylista.databinding.FragmentOrdersBinding
import com.mad43.stylista.ui.category.CategoryAdapter
import com.mad43.stylista.ui.category.CategoryViewModel
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OrdersFragment : Fragment() {

    private var _binding: FragmentOrdersBinding? = null
    private lateinit var ordersViewModel: OrdersViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        ordersViewModel =
            ViewModelProvider(this)[OrdersViewModel::class.java]

        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            ordersViewModel.orders.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        Log.i("Orders ","Loading")
                    }

                    is RemoteStatus.Success -> {
                        Log.i("Orders ","Success")
                        it.data.forEach {
                            Log.i("Orders ","created_at ${it.created_at.toString()}")
                            Log.i("Orders ","price ${it.current_subtotal_price.toString()}")
                        }
                    }

                    else -> {

                    }
                }
            }
        }
    }

}