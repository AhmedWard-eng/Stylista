package com.mad43.stylista.ui.profile.view

import android.content.ContentValues
import android.content.ContentValues.TAG
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
import androidx.recyclerview.widget.LinearLayoutManager
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
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnItemProductClicked {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var profileViewModel : ProfileViewModel
    lateinit var favFactory: ProfileFactoryViewModel
    private lateinit var brandAdapter: AdapterWishList
    var favouriteList= mutableListOf<Favourite>()
    var isLogin = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)

        val authRepo = AuthRepositoryImp()
        favFactory = ProfileFactoryViewModel(AuthUseCase(authRepo), FavouriteLocal(favouriteLocalRepo))
        profileViewModel = ViewModelProvider(this, favFactory)[ProfileViewModel::class.java]
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayUserName()
        profileViewModel.checkUserIsLogin()
        observeLogin()
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
            if (NetwarkInternet().isNetworkAvailable(requireContext())){
                profileViewModel.logout()
                Navigation.findNavController(requireView())
                    .navigate(R.id.action_navigation_profile_to_logInFragment)
            }else{
                NetwarkInternet().displayNetworkDialog(requireContext())
                Log.d(TAG, "check network: ")
            }

        }
    }
    private fun displayUserName(){
        var userName = profileViewModel.getUserName()
        binding.textViewHelloUserName.text = "Welcome! Nice to meet , $userName"
    }

    private fun displayWishList(){
        brandAdapter = AdapterWishList(favouriteList,this@ProfileFragment)
        binding.recyclerViewWishList.adapter = brandAdapter
        binding.recyclerViewWishList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWishList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        if (NetwarkInternet().isNetworkAvailable(requireContext())){
            var favID = profileViewModel.getIDForFavourite()
            profileViewModel.getFavouriteUsingId(favID.toString())
        }else{
            profileViewModel.getLocalFavourite()
        }
        lifecycleScope.launch {

            profileViewModel.favouriteList.collectLatest { uiState ->when (uiState) {
                is RemoteStatus.Success -> {

                    if(uiState.data.size <= 4){
                        brandAdapter.setData(uiState.data)
                        binding.textViewMoreWishList.visibility = View.GONE
                    }
                    else{
                        val firstFourFavourite = uiState.data.take(4)
                        brandAdapter.setData(firstFourFavourite)
                        binding.textViewMoreWishList.visibility = View.VISIBLE
                    }
                }

                else -> {}
            }
            }
        }
        lifecycleScope.launch {
            profileViewModel.uiStateNetwork.collectLatest {
                    uiState ->when (uiState) {
                is RemoteStatus.Success -> {
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
                    if(favouriteList1.size <= 4){
                        brandAdapter.setData(favouriteList1)
                        binding.textViewMoreWishList.visibility = View.GONE
                    }
                    else{
                        val firstFourFavourite = favouriteList1.take(4)
                        brandAdapter.setData(firstFourFavourite)
                        binding.textViewMoreWishList.visibility = View.VISIBLE
                    }
                }
                is RemoteStatus.Failure ->{
                    Log.d(ContentValues.TAG, "Plaese Login,,,,failllllllllllllll:::;: ")
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

    fun observeLogin(){
        lifecycleScope.launch {
            profileViewModel.userExists.collect { userExists ->
                if (userExists) {
                    isLogin = true
                    binding.buttonLogOut.visibility = View.VISIBLE
                    binding.textViewMoreWishList.visibility = View.VISIBLE
                    binding.recyclerViewWishList.visibility = View.VISIBLE
                    binding.textViewWishList.visibility = View.VISIBLE
                    binding.addressesView.visibility = View.VISIBLE
                    binding.separator3.visibility = View.VISIBLE
                    binding.textView3.visibility = View.VISIBLE
                } else {
                    isLogin = false
                    binding.buttonLogOut.visibility = View.GONE
                    binding.textViewMoreWishList.visibility = View.GONE
                    binding.recyclerViewWishList.visibility = View.GONE
                    binding.textViewWishList.visibility = View.GONE
                    binding.addressesView.visibility = View.GONE
                    binding.separator3.visibility = View.GONE
                    binding.textView3.visibility = View.GONE
                }
            }
        }
    }

}