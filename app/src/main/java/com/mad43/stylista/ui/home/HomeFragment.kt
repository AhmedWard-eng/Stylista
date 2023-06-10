package com.mad43.stylista.ui.home

import android.os.Bundle
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
import com.mad43.stylista.databinding.FragmentHomeBinding
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment(), OnItemBrandClicked {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandAdapter: HomeBrandAdapter
    private lateinit var homeViewModel : HomeViewModel

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

        binding.imageSlider.setImageList(homeViewModel.ads, ScaleTypes.FIT)
        binding.imageSlider.startSliding(2000)

        lifecycleScope.launch {
            homeViewModel.brands.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        binding.brandRecycle.visibility = View.GONE
                        binding.shimmerFrameLayout.startShimmerAnimation()
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

                    else -> {}
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun brandClicked(brand: String) {
        val action = HomeFragmentDirections.actionNavigationHomeToBrandFragment2(brand)
        binding.root.findNavController().navigate(action)
    }
}