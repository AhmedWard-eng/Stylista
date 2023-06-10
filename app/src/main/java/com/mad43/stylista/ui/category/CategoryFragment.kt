package com.mad43.stylista.ui.category

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
            ViewModelProvider(this).get(CategoryViewModel::class.java)

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
            categoryViewModel.filterByMainCategory(binding.shoesMainCategoryText.text.toString())

        }

        binding.accessoriesCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.white)
            binding.accessoriesCategory.setBackgroundResource(R.color.primary_color)
            binding.shirtCategory.setBackgroundResource(R.color.white)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.white))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory(binding.accessoriesMainCategoryText.text.toString())
        }

        binding.shirtCategory.setOnClickListener {
            binding.shoesCategory.setBackgroundResource(R.color.white)
            binding.accessoriesCategory.setBackgroundResource(R.color.white)
            binding.shirtCategory.setBackgroundResource(R.color.primary_color)
            binding.shoesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.accessoriesMainCategoryText.setTextColor(resources.getColor(R.color.primary_color))
            binding.shirtMainCategoryText.setTextColor(resources.getColor(R.color.white))
            categoryViewModel.filterMainCategory = true
            categoryViewModel.filterByMainCategory(binding.shirtMainCategoryText.text.toString())
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
                        if (!categoryViewModel.filterSubCategory || !categoryViewModel.filterMainCategory) {
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