package com.mad43.stylista.ui.favourite

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentFavouriteBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.brand.OnItemProductClicked

import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.util.MyDialog
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
            Log.d(TAG, "favIDfavIDfavIDfavID: ${favID}")
            productInfo.getFavouriteUsingId(favID.toString())
        }else{
            productInfo.getLocalFavourite()
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                productInfo.favouriteList.collectLatest { uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        brandAdapter.setData(uiState.data)

                        if (uiState.data.isEmpty() ){
                            binding.imageView.visibility = View.VISIBLE
                            binding.textView7.visibility = View.VISIBLE
                            binding.textViewMyFavourite.visibility = View.VISIBLE
                            binding.RecyclerViewFavourite.visibility = View.GONE
                        }
                        else{
                            binding.imageView.visibility = View.GONE
                            binding.textView7.visibility = View.GONE
                            binding.textViewMyFavourite.visibility = View.VISIBLE
                            binding.RecyclerViewFavourite.visibility = View.VISIBLE
                        }
                    }
                    is RemoteStatus.Loading -> {
                        binding.textViewMyFavourite.visibility = View.VISIBLE
                    }

                    else -> {}
                }
                }
            }

        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                productInfo.uiStateNetwork.collectLatest {
                        uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        binding.textViewMyFavourite.visibility = View.VISIBLE
                        val favouriteSet = mutableSetOf<Favourite>()
                        for (i in 0..((uiState.data.draft_order?.line_items?.size)?.minus(1) ?: 1)){
                            var title = uiState.data.draft_order?.line_items?.get(i)?.title
                            var idProduct = uiState.data.draft_order?.line_items?.get(i)?.product_id
                            var idVarians = uiState.data.draft_order?.line_items?.get(i)?.variant_id
                            var image = uiState.data.draft_order?.line_items?.get(i)?.properties
                            val urlImage = image?.find { it.name == "url_image" }?.value
                            var price = uiState.data.draft_order?.line_items?.get(i)?.price
                            if(idProduct!=null && title!=null && price !=null && urlImage!= null && idVarians !=null){
                                var favourite = Favourite(idProduct,title,price,urlImage,idVarians)
                                favouriteSet += favourite
                            }
                        }
                        val favouriteList1 = favouriteSet.toList()
                        brandAdapter.setData(favouriteList1)
                        if (favouriteList1.isEmpty() ){
                            binding.imageView.visibility = View.VISIBLE
                            binding.textView7.visibility = View.VISIBLE
                            binding.textViewMyFavourite.visibility = View.VISIBLE
                            binding.RecyclerViewFavourite.visibility = View.GONE
                        }
                        else{
                            binding.imageView.visibility = View.GONE
                            binding.textView7.visibility = View.GONE
                            binding.textViewMyFavourite.visibility = View.VISIBLE
                            binding.RecyclerViewFavourite.visibility = View.VISIBLE
                        }

                    }
                    is RemoteStatus.Failure ->{
                        Log.d(TAG, "failllllllllllllll:::;: ")
                        binding.textViewMyFavourite.visibility = View.GONE
                        binding.imageView.visibility = View.GONE
                        binding.textView7.visibility = View.GONE
                        showConfirmationDialog()
                    }
                    is RemoteStatus.Loading -> {
                        binding.textViewMyFavourite.visibility = View.VISIBLE
                    }

                    else -> {
                        Log.d(TAG, "elllllllllllse:::;: ")
                    }
                }
            }

            }
        }
    }


    override fun productClicked(id: Long) {
        val action = FavouriteFragmentDirections.actionFavouriteFragmentToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        var message = "${getString(R.string.check_login)}"
        builder.setMessage(message)
            .setPositiveButton(getString(R.string.yes)) { dialog, which ->
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favouriteFragment_to_logInFragment)
            }
            .setNegativeButton(getString(R.string.cancel)) { dialog, which ->
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_favouriteFragment_to_navigation_home)
            }
        builder.show()
    }

}