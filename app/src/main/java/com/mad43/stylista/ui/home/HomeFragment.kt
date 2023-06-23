package com.mad43.stylista.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.MainActivity
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentHomeBinding
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetworkConnectivity
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), OnItemBrandClicked {

    private lateinit var  _binding: FragmentHomeBinding
    private val binding get() = _binding

    private lateinit var brandAdapter: HomeBrandAdapter
    private lateinit var homeViewModel : HomeViewModel

    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.swipeRefresher.setColorSchemeResources(R.color.primary_color)

        binding.imageSliderHome.setImageList(homeViewModel.ads, ScaleTypes.FIT)
        binding.imageSliderHome.startSliding(2000)

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
            homeViewModel.brands.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        if (networkConnectivity.isOnline()) {
                            binding.brandRecycle.visibility = View.GONE
                            binding.shimmerFrameLayout.visibility = View.VISIBLE
                            binding.shimmerFrameLayout.startShimmerAnimation()
                            binding.connectivity.visibility = View.VISIBLE
                            binding.noConnectivity.visibility = View.GONE

                        }else{
                            binding.connectivity.visibility = View.GONE
                            binding.noConnectivity.visibility = View.VISIBLE
                        }
                    }

                    is RemoteStatus.Success -> {
                        binding.brandRecycle.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        brandAdapter =
                            HomeBrandAdapter(requireContext(),this@HomeFragment)
                        binding.brandRecycle.apply {
                            adapter = brandAdapter
                            brandAdapter.submitList(it.data)
                            layoutManager = GridLayoutManager(context,2).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }

                    else -> {
                        if (!networkConnectivity.isOnline()) {
                            binding.connectivity.visibility = View.GONE
                            binding.noConnectivity.visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
    }

    override fun brandClicked(brand: String) {
        if (networkConnectivity.isOnline()) {
            val action = HomeFragmentDirections.actionNavigationHomeToBrandFragment2(brand)
            binding.root.findNavController().navigate(action)
        }else{
            val dialog = MyDialog()
            dialog.showAlertDialog("Please, check your connection", requireContext())
        }
    }

    private fun refresh(){
        if (networkConnectivity.isOnline()) {
            binding.noConnectivity.visibility = View.GONE
            binding.connectivity.visibility = View.VISIBLE
            homeViewModel.brands.value = RemoteStatus.Loading
            homeViewModel.getBrand()

        } else {
            binding.connectivity.visibility = View.GONE
            binding.noConnectivity.visibility = View.VISIBLE
        }

        binding.swipeRefresher.isRefreshing = false
    }

}