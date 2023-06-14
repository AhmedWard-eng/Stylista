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
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentLogInBinding
import com.mad43.stylista.databinding.FragmentSearchBinding
import com.mad43.stylista.domain.model.DisplayBrand
import com.mad43.stylista.ui.brand.BrandAdapter
import com.mad43.stylista.ui.home.HomeBrandAdapter
import com.mad43.stylista.ui.home.HomeFragmentDirections
import com.mad43.stylista.ui.home.HomeViewModel
import com.mad43.stylista.ui.home.OnItemBrandClicked
import com.mad43.stylista.ui.search.viewModel.SearchViewModel
import com.mad43.stylista.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment() , OnItemBrandClicked {

    private lateinit var searchViewModel: SearchViewModel
    private lateinit var brandAdapter: HomeBrandAdapter
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchViewModel = ViewModelProvider(this)[SearchViewModel::class.java]

        val activity = requireActivity() as AppCompatActivity
        val toolbar = activity.findViewById<androidx.appcompat.widget.Toolbar>(R.id.materialToolbar)
        activity.setSupportActionBar(toolbar)

        brandAdapter = HomeBrandAdapter(requireContext(), this@SearchFragment)
        binding.searchByNameRecyclerView.adapter = brandAdapter

        binding.searchByNameRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        var searchView = toolbar.findViewById<android.widget.EditText>(R.id.searchView)
        searchView.hint = getString(R.string.search_brand)
        searchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                searchViewModel.getBrand()
                brandAdapter.submitList(searchViewModel.brands)
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchTextContent = searchView.text.toString()
                val filteredBrands = searchViewModel.searchBrand(searchTextContent)
                brandAdapter.submitList(filteredBrands)

            }
            override fun afterTextChanged(s: Editable?) {
                val searchTextContent = searchView.text.toString()
                val filteredBrands = searchViewModel.searchBrand(searchTextContent)
                brandAdapter.submitList(filteredBrands)
            }
        })
    }
    override fun brandClicked(brand: String) {
        val action = SearchFragmentDirections.actionSearchFragmentToBrandFragment(brand)
        binding.root.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}