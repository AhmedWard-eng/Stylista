package com.mad43.stylista.ui.search.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentSearchBinding
import com.mad43.stylista.databinding.FragmentSearchProductBinding
import com.mad43.stylista.ui.brand.BrandAdapter
import com.mad43.stylista.ui.brand.BrandFragmentDirections
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.ui.home.HomeBrandAdapter
import com.mad43.stylista.ui.search.viewModel.SearchViewModel


class SearchProductFragment : Fragment() , OnItemProductClicked {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var brandAdapter: BrandAdapter
    private var _binding: FragmentSearchProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        val brand = arguments?.getString("brand")

        val activity = requireActivity() as AppCompatActivity
        val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.materialToolbar)
        activity.setSupportActionBar(toolbar)

        brandAdapter = BrandAdapter(requireContext(),this@SearchProductFragment)
        binding.searchByNameRecyclerView.adapter = brandAdapter
        binding.searchByNameRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        var searchView = toolbar.findViewById<android.widget.EditText>(R.id.searchView)
        searchView.hint = getString(R.string.search_product)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchViewModel.getAllProduct()
                    brandAdapter.submitList(searchViewModel.allPrpduct)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTextContent = searchView.text.toString()
                val filteredProducts = searchViewModel.searchAllProduct(searchTextContent)
                brandAdapter.submitList(filteredProducts)
            }
            override fun afterTextChanged(s: Editable?) {
                val searchTextContent = searchView.text.toString()
                val filteredProducts = searchViewModel.searchAllProduct(searchTextContent)
                brandAdapter.submitList(filteredProducts)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun productClicked(id: Long) {
        val action = SearchProductFragmentDirections.actionSearchProductFragmentToProductDetailsFragment3(id)
        binding.root.findNavController().navigate(action)
    }

}