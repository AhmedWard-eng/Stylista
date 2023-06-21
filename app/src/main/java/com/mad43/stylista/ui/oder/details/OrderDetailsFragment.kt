package com.mad43.stylista.ui.oder.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.databinding.FragmentOrderDetailsBinding
import com.mad43.stylista.databinding.FragmentOrdersBinding
import com.mad43.stylista.ui.brand.BrandAdapter
import com.mad43.stylista.ui.orders.OrdersFragmentDirections
import com.mad43.stylista.ui.orders.OrdersViewModel
import com.mad43.stylista.util.setPrice

class OrderDetailsFragment : Fragment(), OnItemProductOrderClicked {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var  productsDetailsAdapter : OrderDetailsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).order

        binding.orderNumberValueDetails.text = order.number.toString()
        binding.createdValueDetails.text = order.created_at.toString()
        binding.priceValueDetails.setPrice(order.current_subtotal_price.toString().toDouble())

        if (order.lineItems?.isNotEmpty() == true) {
            productsDetailsAdapter = OrderDetailsAdapter(this@OrderDetailsFragment)
            binding.detailsOrdersProducts.apply {
                adapter = productsDetailsAdapter
                productsDetailsAdapter.submitList(order.lineItems)
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
            }
        }
    }

    override fun productOrderClicked(id: Long) {
        val action = OrderDetailsFragmentDirections.actionOrderDetailsFragment2ToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }

}