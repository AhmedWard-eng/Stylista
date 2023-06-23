package com.mad43.stylista.ui.category

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
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentCategoryBinding
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.util.NetworkConnectivity
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(), OnItemProductClicked {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private var subCategory = "kid"
    private val binding get() = _binding!!

    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        categoryViewModel =
            ViewModelProvider(this)[CategoryViewModel::class.java]

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)

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

        binding.shoesCategory.setBackgroundResource(R.color.primary_color)
        binding.accessoriesCategory.setBackgroundResource(R.color.white)
        binding.shirtCategory.setBackgroundResource(R.color.white)
        binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.white))
        binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
        binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))

        binding.kid.setBackgroundResource(R.color.primary_color)
        binding.men.setBackgroundResource(R.color.white)
        binding.women.setBackgroundResource(R.color.white)
        binding.sale.setBackgroundResource(R.color.white)
        binding.kid.setTextColor(resources.getColor(R.color.white))
        binding.men.setTextColor(resources.getColor(R.color.primary_color))
        binding.women.setTextColor(resources.getColor(R.color.primary_color))
        binding.sale.setTextColor(resources.getColor(R.color.primary_color))

        binding.shoesCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.primary_color)
            binding.accessoriesCategory.setBackgroundResource(R.color.white)
            binding.shirtCategory.setBackgroundResource(R.color.white)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.white))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory("SHOES")
            categoryViewModel.filterBySubCategory(subCategory)
        }

        binding.accessoriesCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.white)
            binding.accessoriesCategory.setBackgroundResource(R.color.primary_color)
            binding.shirtCategory.setBackgroundResource(R.color.white)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.white))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory("ACCESSORIES")
            categoryViewModel.filterBySubCategory(subCategory)
        }

        binding.shirtCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.white)
            binding.accessoriesCategory.setBackgroundResource(R.color.white)
            binding.shirtCategory.setBackgroundResource(R.color.primary_color)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.white))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory("T-SHIRTS")
            categoryViewModel.filterBySubCategory(subCategory)
        }

        binding.kid.setOnClickListener {
            binding.kid.setBackgroundResource(R.color.primary_color)
            binding.men.setBackgroundResource(R.color.white)
            binding.women.setBackgroundResource(R.color.white)
            binding.sale.setBackgroundResource(R.color.white)
            binding.kid.setTextColor(resources.getColor(R.color.white))
            binding.men.setTextColor(resources.getColor(R.color.primary_color))
            binding.women.setTextColor(resources.getColor(R.color.primary_color))
            binding.sale.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterSubCategory = true
            subCategory = "kid"
            categoryViewModel.filterBySubCategory("kid")

        }

        binding.men.setOnClickListener {
            binding.kid.setBackgroundResource(R.color.white)
            binding.men.setBackgroundResource(R.color.primary_color)
            binding.women.setBackgroundResource(R.color.white)
            binding.sale.setBackgroundResource(R.color.white)
            binding.kid.setTextColor(resources.getColor(R.color.primary_color))
            binding.men.setTextColor(resources.getColor(R.color.white))
            binding.women.setTextColor(resources.getColor(R.color.primary_color))
            binding.sale.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterSubCategory = true
            subCategory = "men"
            categoryViewModel.filterBySubCategory("men")
        }

        binding.women.setOnClickListener {
            binding.kid.setBackgroundResource(R.color.white)
            binding.men.setBackgroundResource(R.color.white)
            binding.women.setBackgroundResource(R.color.primary_color)
            binding.sale.setBackgroundResource(R.color.white)
            binding.kid.setTextColor(resources.getColor(R.color.primary_color))
            binding.men.setTextColor(resources.getColor(R.color.primary_color))
            binding.women.setTextColor(resources.getColor(R.color.white))
            binding.sale.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterSubCategory = true
            subCategory = "women"
            categoryViewModel.filterBySubCategory("women")
        }

        binding.sale.setOnClickListener {
            binding.kid.setBackgroundResource(R.color.white)
            binding.men.setBackgroundResource(R.color.white)
            binding.women.setBackgroundResource(R.color.white)
            binding.sale.setBackgroundResource(R.color.primary_color)
            binding.kid.setTextColor(resources.getColor(R.color.primary_color))
            binding.men.setTextColor(resources.getColor(R.color.primary_color))
            binding.women.setTextColor(resources.getColor(R.color.primary_color))
            binding.sale.setTextColor(resources.getColor(R.color.white))
            categoryViewModel.filterSubCategory = true
            subCategory = "sale"
            categoryViewModel.filterBySubCategory("sale")
        }

        lifecycleScope.launch {
            categoryViewModel.products.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        if (networkConnectivity.isOnline()) {
                            binding.recyclerView.visibility = View.GONE
                            binding.shimmerFrameLayout.startShimmerAnimation()
                            binding.noConnectivity.visibility = View.GONE
                        }else{
                            binding.noConnectivity.visibility = View.VISIBLE
                            binding.connectivity.visibility = View.GONE
                        }
                    }

                    is RemoteStatus.Success -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        if (!categoryViewModel.filterMainCategory) {
                            if (it.data.isNotEmpty()) {
                                categoryViewModel.allData = it.data
                                categoryViewModel.filterMainCategory = true
                                categoryViewModel.filterByMainCategory("SHOES")
                                categoryViewModel.filterSubCategory = true
                                subCategory = "kid"
                                categoryViewModel.filterBySubCategory(subCategory)
                            }else{
                                categoryViewModel.getProducts()
                            }
                        }

                        categoryAdapter = CategoryAdapter(this@CategoryFragment)
                        binding.recyclerView.apply {
                            adapter = categoryAdapter
                            categoryAdapter.submitList(it.data)
                            layoutManager = GridLayoutManager(context, 2).apply {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun productClicked(id: Long) {
        val action =
            CategoryFragmentDirections.actionNavigationDashboardToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }

    private fun refresh(){
        if (networkConnectivity.isOnline()) {
            binding.connectivity.visibility = View.VISIBLE
            binding.noConnectivity.visibility = View.GONE

            binding.shoesCategory.setBackgroundResource(R.color.primary_color)
            binding.accessoriesCategory.setBackgroundResource(R.color.white)
            binding.shirtCategory.setBackgroundResource(R.color.white)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.white))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))

            binding.kid.setBackgroundResource(R.color.primary_color)
            binding.men.setBackgroundResource(R.color.white)
            binding.women.setBackgroundResource(R.color.white)
            binding.sale.setBackgroundResource(R.color.white)
            binding.kid.setTextColor(resources.getColor(R.color.white))
            binding.men.setTextColor(resources.getColor(R.color.primary_color))
            binding.women.setTextColor(resources.getColor(R.color.primary_color))
            binding.sale.setTextColor(resources.getColor(R.color.primary_color))

            categoryViewModel.filterMainCategory = false
            categoryViewModel.getProducts()

        } else {
            binding.connectivity.visibility = View.GONE
            binding.noConnectivity.visibility = View.VISIBLE
        }

        binding.swipeRefresher.isRefreshing = false
    }
}