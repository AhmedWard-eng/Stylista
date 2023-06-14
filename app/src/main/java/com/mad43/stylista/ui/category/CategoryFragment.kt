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
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CategoryFragment : Fragment(), OnItemProductClicked {

    private var _binding: FragmentCategoryBinding? = null
    private lateinit var categoryViewModel: CategoryViewModel
    private lateinit var categoryAdapter: CategoryAdapter
    private val binding get() = _binding!!

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

        binding.shoesCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.primary_color)
            binding.accessoriesCategory.setBackgroundResource(R.color.white)
            binding.shirtCategory.setBackgroundResource(R.color.white)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.white))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory("SHOES")

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
            categoryViewModel.filterBySubCategory("sale")
        }

        lifecycleScope.launch {
            categoryViewModel.products.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        binding.recyclerView.visibility = View.GONE
                        binding.shimmerFrameLayout.startShimmerAnimation()
                    }

                    is RemoteStatus.Success -> {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.shimmerFrameLayout.visibility = View.GONE
                        binding.shimmerFrameLayout.stopShimmerAnimation()
                        if (!categoryViewModel.filterMainCategory) {
                            categoryViewModel.allData = it.data
                        }

                        categoryAdapter = CategoryAdapter(requireContext(), this@CategoryFragment)
                        binding.recyclerView.apply {
                            adapter = categoryAdapter
                            categoryAdapter.submitList(it.data)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun productClicked(id: Long) {
        val action =
            CategoryFragmentDirections.actionNavigationDashboardToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }
}