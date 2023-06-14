package com.mad43.stylista.ui.search.view

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentSearchBinding
import com.mad43.stylista.ui.home.HomeBrandAdapter
import com.mad43.stylista.ui.home.OnItemBrandClicked
import com.mad43.stylista.ui.search.viewModel.SearchViewModel


class SearchBrandFragment : Fragment() , OnItemBrandClicked {

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

        brandAdapter = HomeBrandAdapter(requireContext(), this@SearchBrandFragment)
        binding.searchByNameRecyclerView.adapter = brandAdapter

        binding.searchByNameRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        val searchView = toolbar.findViewById<android.widget.EditText>(R.id.searchView)
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
        val action = SearchBrandFragmentDirections.actionSearchFragmentToBrandFragment(brand)
        binding.root.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}