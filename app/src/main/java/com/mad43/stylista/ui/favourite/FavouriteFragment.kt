package com.mad43.stylista.ui.favourite

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentFavouriteBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.brand.OnItemProductClicked

import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.util.NetwarkInternet
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


        if(NetwarkInternet().isNetworkAvailable(requireContext())){
            var favID = productInfo.getIDForFavourite()
            productInfo.getFavouriteUsingId(favID.toString())
        }else{
            productInfo.getLocalFavourite()
        }

        lifecycleScope.launch {

            productInfo.favouriteList.collectLatest { uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        brandAdapter.setData(uiState.data)
                    }
                else -> {}
                }
            }
        }
        lifecycleScope.launch {
            productInfo.uiStateNetwork.collectLatest {
                    uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        for (i in 0..((uiState.data.draft_order?.line_items?.size)?.minus(1) ?: 1)){
                            var title = uiState.data.draft_order?.line_items?.get(i)?.title
                            var idProduct = uiState.data.draft_order?.line_items?.get(i)?.product_id
                            var idVarians = uiState.data.draft_order?.line_items?.get(i)?.variant_id
                            var image = uiState.data.draft_order?.line_items?.get(i)?.properties
                            val urlImage = image?.find { it.name == "url_image" }?.value
                            var price = uiState.data.draft_order?.line_items?.get(i)?.price
                            if(idProduct!=null && title!=null && price !=null && urlImage!= null && idVarians !=null){
                                var favourite = Favourite(idProduct,title,price,urlImage,idVarians)
                                favouriteList.add(favourite)

                            }

                        }
                        brandAdapter.setData(favouriteList)
                    }
                is RemoteStatus.Failure ->{
                    Log.d(TAG, "failllllllllllllll:::;: ")
                }
                else -> {
                    Log.d(TAG, "elllllllllllse:::;: ")
                }
                }
            }
        }
    }


    override fun productClicked(id: Long) {
        val action = FavouriteFragmentDirections.actionFavouriteFragmentToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }


}