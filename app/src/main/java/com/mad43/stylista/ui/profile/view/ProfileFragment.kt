package com.mad43.stylista.ui.profile.view

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentProfileBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.ui.favourite.AdapterFavourite
import com.mad43.stylista.ui.profile.viewModel.ProfileFactoryViewModel
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnItemProductClicked {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var profileViewModel : ProfileViewModel
    lateinit var favFactory: ProfileFactoryViewModel
    private lateinit var brandAdapter: AdapterFavourite
    var favouriteList= mutableListOf<Favourite>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)


        binding.textViewHelloUserName.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.ordersFragment)
        }


        val authRepo = AuthRepositoryImp()
        favFactory = ProfileFactoryViewModel(AuthUseCase(authRepo), FavouriteLocal(favouriteLocalRepo))
        profileViewModel = ViewModelProvider(this, favFactory)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserName()
        displayLogout()
        displayWishList()
        displayAllFavourite()


        binding.textViewCurrencyCode.text = profileViewModel.getCurrencyCode()
        binding.currencyView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_currencyFragment)
        }

        binding.addressesView.setOnClickListener {
            Log.d("TAG", "onViewCreated: addressesView")
        }

        lifecycleScope.launch {
            profileViewModel.orders.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        Log.i("Orders ","Loading")
                    }

                    is RemoteStatus.Success -> {
                        Log.i("Orders ","Success")
                        it.data.forEach {
                            Log.i("Orders ","created_at ${it.created_at.toString()}")
                            Log.i("Orders ","price ${it.current_subtotal_price.toString()}")
                        }
                    }
                    else -> {

                    }
                }
            }
        }


    }
    private fun displayLogout(){
        binding.buttonLogOut.setOnClickListener {
            profileViewModel.logout()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_logInFragment)
        }
    }
    private fun displayUserName(){
        var userName = profileViewModel.getUserName()
        binding.textViewHelloUserName.text = "Welcome, $userName"
    }
    private fun displayWishList(){
        brandAdapter = AdapterFavourite(favouriteList,this@ProfileFragment)
        binding.recyclerViewWishList.adapter = brandAdapter
        binding.recyclerViewWishList.layoutManager = GridLayoutManager(requireContext(), 2)

        var favID = profileViewModel.getIDForFavourite()
        profileViewModel.getFavouriteUsingId(favID.toString())

        lifecycleScope.launch {
            profileViewModel.uiStateNetwork.collectLatest {
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
                    brandAdapter.setData(favouriteList.take(4))
                }
                is RemoteStatus.Failure ->{
                    Log.d(ContentValues.TAG, "failllllllllllllll:::;: ")
                }
                else -> {
                    Log.d(ContentValues.TAG, "elllllllllllse:::;: ")
                }
            }
            }
        }
    }
    private fun displayAllFavourite(){
        binding.textViewMoreWishList.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.favouriteFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun productClicked(id: Long) {
        val action = ProfileFragmentDirections.actionNavigationProfileToProductDetailsFragment(id)
        binding.root.findNavController().navigate(action)
    }
}