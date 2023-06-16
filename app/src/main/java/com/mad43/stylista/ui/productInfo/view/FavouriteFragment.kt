package com.mad43.stylista.ui.productInfo.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentFavouriteBinding
import com.mad43.stylista.databinding.FragmentProductDetailsBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.brand.BrandAdapter
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteFragment : Fragment() , OnItemProductClicked {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var brandAdapter: AdapterFavourite
    lateinit var productInfo : ProductInfoViewModel
    lateinit var favFactory: ProductInfoViewModelFactory
    var favouriteList= mutableListOf<Favourite>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)
        favFactory = ProductInfoViewModelFactory(ProductInfo(), FavouriteLocal(favouriteLocalRepo))
        productInfo = ViewModelProvider(this, favFactory).get(ProductInfoViewModel::class.java)
        brandAdapter = AdapterFavourite(favouriteList,this@FavouriteFragment)
        binding.RecyclerViewFavourite.adapter = brandAdapter
        binding.RecyclerViewFavourite.layoutManager = GridLayoutManager(requireContext(), 2)

        productInfo.getLocalFavourite()
        lifecycleScope.launch {

            productInfo.favouriteList.collectLatest { uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        brandAdapter.setData(uiState.data)
                    }
                else -> {}
                }
            }
        }
    }

    override fun productClicked(id: Long) {
        //navigate
    }


}