package com.mad43.stylista.ui.oder.details

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.databinding.FragmentOrderDetailsBinding
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetworkConnectivity
import com.mad43.stylista.util.RemoteStatus
import com.mad43.stylista.util.formatDate
import com.mad43.stylista.util.setPrice

class OrderDetailsFragment : Fragment(), OnItemProductOrderClicked {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var  productsDetailsAdapter : OrderDetailsAdapter

    var order : Orders? = null
    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
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

        order = OrderDetailsFragmentArgs.fromBundle(requireArguments()).order

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

        showData()

    }

    override fun productOrderClicked(id: Long) {
        if (networkConnectivity.isOnline()) {
            val action =
                OrderDetailsFragmentDirections.actionOrderDetailsFragment2ToProductDetailsFragment(
                    id
                )
            binding.root.findNavController().navigate(action)
        }else{
            val dialog  = MyDialog()
            dialog.showAlertDialog("Please, check your connection", requireContext())
        }
    }

    private fun refresh(){
        if (networkConnectivity.isOnline()) {
            binding.noConnectivity.visibility = View.GONE
            binding.connectivity.visibility = View.VISIBLE
            showData()
        } else {
            binding.connectivity.visibility = View.GONE
            binding.noConnectivity.visibility = View.VISIBLE
        }

        binding.swipeRefresher.isRefreshing = false
    }

    private fun showData(){
        binding.orderNumberValueDetails.text = order?.number.toString()
        binding.createdValueDetails.formatDate(order?.created_at.toString())
        binding.priceValueDetails.setPrice(order?.current_subtotal_price.toString().toDouble())

        if (order?.lineItems?.isNotEmpty() == true) {
            productsDetailsAdapter = OrderDetailsAdapter(this@OrderDetailsFragment)
            binding.detailsOrdersProducts.apply {
                adapter = productsDetailsAdapter
                productsDetailsAdapter.submitList(order?.lineItems)
                layoutManager = LinearLayoutManager(context).apply {
                    orientation = RecyclerView.VERTICAL
                }
            }
        }
    }

}