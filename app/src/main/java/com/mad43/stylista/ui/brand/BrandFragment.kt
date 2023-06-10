package com.mad43.stylista.ui.brand

import android.annotation.SuppressLint
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentBrandBinding
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class BrandFragment : Fragment(), OnItemProductClicked {

    private var _binding: FragmentBrandBinding? = null
    private val binding get() = _binding!!
    private lateinit var brandAdapter: BrandAdapter
    private lateinit var brandViewModel: BrandViewModel
    var priceClicked = false
    var categoryClicked = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBrandBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        brandViewModel =
            ViewModelProvider(this)[BrandViewModel::class.java]

        val brand = arguments?.getString("brand")

        binding.filterValue.visibility = View.GONE

        seekBarConfigration()

        binding.priceFilter.setOnClickListener {
            if (!priceClicked) {
                priceClicked = true
                categoryClicked = false
                brandViewModel.filter = true
                brandViewModel.dataFiltered = brandViewModel.allData
                binding.priceFilter.setBackgroundResource(R.color.primary_color)
                binding.categoryFilter.setBackgroundResource(R.color.white)
                binding.priceFilter.setTextColor(resources.getColor(R.color.white))
                binding.categoryFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.filterValue.visibility = View.VISIBLE
                binding.seekBar.visibility = View.VISIBLE
                binding.categoryGroup.visibility = View.GONE
                brandViewModel.filterByPrice(
                    String.format(
                        "%.2f",
                        binding.seekBar.progress.toFloat()
                    )
                )
            } else {
                priceClicked = false
                categoryClicked = false
                brandViewModel.filter = false
                binding.priceFilter.setBackgroundResource(R.color.white)
                binding.categoryFilter.setBackgroundResource(R.color.white)
                binding.priceFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.categoryFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.seekBar.visibility = View.GONE
                brandViewModel.filterByPrice("0.00")
            }
        }

        binding.seekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean,
            ) {
                if (progress != 0) {
                    val price = String.format("%.2f", progress.toFloat())
                    brandViewModel.filterByPrice(price)
                } else {
                    brandViewModel.filterByPrice("")
                }
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {

            }
        })

        binding.categoryFilter.setOnClickListener {
            if (!categoryClicked) {
                categoryClicked = true
                priceClicked = false
                brandViewModel.filter = true
                brandViewModel.dataFiltered = brandViewModel.allData
                binding.priceFilter.setBackgroundResource(R.color.white)
                binding.categoryFilter.setBackgroundResource(R.color.primary_color)
                binding.priceFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.categoryFilter.setTextColor(resources.getColor(R.color.white))
                binding.filterValue.visibility = View.VISIBLE
                binding.categoryGroup.visibility = View.VISIBLE
                binding.seekBar.visibility = View.GONE
            } else {
                categoryClicked = false
                priceClicked = false
                brandViewModel.filter = false
                brandViewModel.dataFiltered = brandViewModel.allData
                binding.priceFilter.setBackgroundResource(R.color.white)
                binding.categoryFilter.setBackgroundResource(R.color.white)
                binding.priceFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.categoryFilter.setTextColor(resources.getColor(R.color.primary_color))
                binding.categoryGroup.visibility = View.GONE
            }
        }

        binding.shoes.setOnClickListener {
            brandViewModel.filterByCategory(binding.shoes.text.toString())
        }

        binding.accessories.setOnClickListener {
            brandViewModel.filterByCategory(binding.accessories.text.toString())
        }

        binding.shirt.setOnClickListener {
            brandViewModel.filterByCategory(binding.shirt.text.toString())
        }

        brandViewModel.getProductInBrand(brand ?: "")

        lifecycleScope.launch {
            brandViewModel.products.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.shimmerFrameLayoutBrand.startShimmerAnimation()
                    }

                    is RemoteStatus.Success -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.shimmerFrameLayoutBrand.visibility = View.GONE
                        binding.shimmerFrameLayoutBrand.stopShimmerAnimation()
                        if (!brandViewModel.filter) {
                            brandViewModel.allData = it.data
                        }
                        brandAdapter = BrandAdapter(requireContext(), this@BrandFragment)
                        binding.recyclerView.apply {
                            adapter = brandAdapter
                            brandAdapter.submitList(it.data)
                            layoutManager = GridLayoutManager(context, 2).apply {
                                orientation = RecyclerView.VERTICAL
                            }
                        }
                    }

                    else -> {}
                }
            }
        }

    }

    override fun productClicked(id: Long) {
        val action = BrandFragmentDirections.actionBrandFragmentToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }

    private fun seekBarConfigration() {
        binding.seekBar.visibility = View.GONE

        binding.seekBar.progressDrawable
            .setColorFilter(resources.getColor(R.color.primary_color), PorterDuff.Mode.SRC_ATOP)

        binding.seekBar.thumb.setColorFilter(
            resources.getColor(R.color.primary_color),
            PorterDuff.Mode.SRC_ATOP
        )

    }

}