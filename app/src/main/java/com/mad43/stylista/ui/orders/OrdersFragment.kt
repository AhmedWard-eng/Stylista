package com.mad43.stylista.ui.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.databinding.FragmentCategoryBinding
import com.mad43.stylista.databinding.FragmentOrdersBinding
import com.mad43.stylista.ui.brand.BrandAdapter
import com.mad43.stylista.ui.brand.BrandViewModel
import com.mad43.stylista.ui.category.CategoryAdapter
import com.mad43.stylista.ui.category.CategoryViewModel
import com.mad43.stylista.ui.home.HomeFragmentDirections
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetworkConnectivity
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext

class OrdersFragment : Fragment(), OnItemOrderClicked {

    private var _binding: FragmentOrdersBinding? = null
    private lateinit var ordersViewModel: OrdersViewModel
    private val binding get() = _binding!!
    private lateinit var ordersAdapter: OrdersAdapter

    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
    }
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

        binding.swipeRefresher.setColorSchemeResources(R.color.primary_color)

        if (networkConnectivity.isOnline()) {
            binding.connectivity.visibility = View.VISIBLE
            binding.noConnectivity.visibility = View.GONE
        } else {
            binding.connectivity.visibility = View.GONE
            binding.noConnectivity.visibility = View.VISIBLE
        }

        binding.swipeRefresher.setOnRefreshListener {
            refresh()
        }

        lifecycleScope.launch {
            ordersViewModel.orders.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        if (networkConnectivity.isOnline()) {
                            binding.connectivity.visibility = View.VISIBLE
                            binding.noConnectivity.visibility = View.GONE
                            binding.recycleOrders.visibility = View.GONE
                            binding.shimmerFrameLayoutOrders.startShimmerAnimation()
                        }else{
                            binding.connectivity.visibility = View.GONE
                            binding.noConnectivity.visibility = View.VISIBLE
                        }
                    }

                    is RemoteStatus.Success -> {
                        binding.recycleOrders.visibility = View.VISIBLE
                        binding.shimmerFrameLayoutOrders.visibility = View.GONE
                        binding.shimmerFrameLayoutOrders.stopShimmerAnimation()

                        ordersAdapter = OrdersAdapter(this@OrdersFragment)
                        binding.recycleOrders.apply {
                            adapter = ordersAdapter
                            ordersAdapter.submitList(it.data)
                            layoutManager = LinearLayoutManager(context).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }

                    else -> {
                        if (!networkConnectivity.isOnline()) {
                            binding.noConnectivity.visibility = View.GONE
                            binding.connectivity.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun orderClicked(orders: Orders) {
        if (networkConnectivity.isOnline()) {
            val action =
                OrdersFragmentDirections.actionOrdersFragmentToOrderDetailsFragment2(orders)
            binding.root.findNavController().navigate(action)
        }else{
            val dialog = MyDialog()
            dialog.showAlertDialog("Please, check your connection", requireContext())
        }
    }

    private fun refresh() {
        if (networkConnectivity.isOnline()) {
            binding.connectivity.visibility = View.VISIBLE
            binding.noConnectivity.visibility = View.GONE

            ordersViewModel.getOrders()
        } else {
            binding.connectivity.visibility = View.GONE
            binding.noConnectivity.visibility = View.VISIBLE
        }

        binding.swipeRefresher.isRefreshing = false
    }




}