package com.mad43.stylista.ui.profile.view

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.remote.entity.orders.Orders
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentProfileBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.brand.OnItemProductClicked
import com.mad43.stylista.ui.favourite.AdapterFavourite
import com.mad43.stylista.ui.orders.OnItemOrderClicked
import com.mad43.stylista.ui.orders.OrdersAdapter
import com.mad43.stylista.ui.orders.OrdersFragmentDirections
import com.mad43.stylista.ui.profile.viewModel.ProfileFactoryViewModel
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.NetworkConnectivity
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(), OnItemProductClicked, OnItemOrderClicked {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var profileViewModel: ProfileViewModel
    lateinit var favFactory: ProfileFactoryViewModel
    private lateinit var brandAdapter: AdapterWishList
    private lateinit var orderAdapter: OrdersProfileAdapter
    var favouriteList = mutableListOf<Favourite>()
    var isLogin = false

    private val networkConnectivity by lazy {
        NetworkConnectivity.getInstance(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)


        val authRepo = AuthRepositoryImp()
        favFactory =
            ProfileFactoryViewModel(AuthUseCase(authRepo), FavouriteLocal(favouriteLocalRepo))
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

        binding.swipeRefresher.setColorSchemeResources(R.color.primary_color)

        binding.textViewMoreOrders.setOnClickListener {
            Navigation.findNavController(requireView()).navigate(R.id.ordersFragment)
        }

        binding.swipeRefresher.setOnRefreshListener {
            refresh()
        }

        binding.textViewCurrencyCode.text = profileViewModel.getCurrencyCode()
        binding.currencyView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_currencyFragment)
        }

        binding.addressesView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_addressListFragment)
        }

        lifecycleScope.launch {
            profileViewModel.orders.collectLatest {
                when (it) {
                    is RemoteStatus.Loading -> {
                        binding.recyclerViewOrders.visibility = View.GONE
                        binding.textViewMoreOrders.visibility = View.GONE
                    }

                    is RemoteStatus.Success -> {
                        binding.recyclerViewOrders.visibility = View.VISIBLE
                        lifecycleScope.launch {

                            orderAdapter = OrdersProfileAdapter(this@ProfileFragment)
                            binding.recyclerViewOrders.apply {
                                adapter = orderAdapter
                                var list: MutableList<Orders> = mutableListOf()
                                if (it.data.isNotEmpty()) {
                                    if (it.data.size == 2) {
                                        list.add(it.data[0])
                                        list.add(it.data[1])
                                        binding.textViewMoreOrders.visibility = View.GONE
                                    } else if (it.data.size > 2) {
                                        list.add(it.data[0])
                                        list.add(it.data[1])
                                        binding.textViewMoreOrders.visibility = View.VISIBLE
                                    } else {
                                        list.add(it.data[0])
                                    }
                                }
                                orderAdapter.submitList(list)
                                layoutManager = LinearLayoutManager(context).apply {
                                    orientation = RecyclerView.HORIZONTAL
                                }
                            }
                        }
                    }

                    else -> {
                        if (!networkConnectivity.isOnline()) {
                            binding.recyclerViewOrders.visibility = View.GONE
                            binding.textViewMoreOrders.visibility = View.GONE
                        }
                    }
                }
            }
        }


    }

    private fun displayLogout() {
        binding.buttonLogOut.setOnClickListener {
            if (NetwarkInternet().isNetworkAvailable(requireContext())) {
                if (binding.buttonLogOut.text == getString(R.string.logout)) {
                    profileViewModel.logout()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_profile_to_logInFragment)
                }
                if (binding.buttonLogOut.text == getString(R.string.sign_in)) {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_navigation_profile_to_logInFragment)
                }
            } else {
                NetwarkInternet().displayNetworkDialog(requireContext())
                Log.d(TAG, "check network: ")
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun displayUserName() {
        val userName = profileViewModel.getUserName()
        val message = getString(R.string.welcome)
        binding.textViewHelloUserName.text = "$message , $userName"

    }

    private fun displayWishList() {
        brandAdapter = AdapterWishList(favouriteList, this@ProfileFragment)
        binding.recyclerViewWishList.adapter = brandAdapter
        binding.recyclerViewWishList.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewWishList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        if (NetwarkInternet().isNetworkAvailable(requireContext())) {
            var favID = profileViewModel.getIDForFavourite()
            profileViewModel.getFavouriteUsingId(favID.toString())
        } else {
            profileViewModel.getLocalFavourite()
        }
        lifecycleScope.launch {


            profileViewModel.favouriteList.collectLatest { uiState ->
                when (uiState) {
                    is RemoteStatus.Success -> {

                        if (uiState.data.size <= 4) {
                            brandAdapter.setData(uiState.data)
                            binding.textViewMoreWishList.visibility = View.GONE
                        } else {
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

            profileViewModel.uiStateNetwork.collectLatest { uiState ->
              when (uiState) {
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
                            Log.d(TAG, "DDDDDDDDDDDDDDDDDdisplayWishList: ${title}")
                            favouriteSet += favourite
                        }
                    }
                }
                    is RemoteStatus.Failure -> {
                        Log.d(ContentValues.TAG, "Plaese Login,,,,failllllllllllllll:::;: ")
                    }

                    else -> {
                        Log.d(ContentValues.TAG, "elllllllllllse:::;: ")
                    }
                }
            }
        }
    }

    private fun displayAllFavourite() {
        binding.textViewMoreWishList.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_favouriteFragment)
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

    fun observeLogin() {
        lifecycleScope.launch {
            profileViewModel.userExists.collect { userExists ->
                if (userExists) {
                    isLogin = true
                    //    binding.buttonLogOut.visibility = View.VISIBLE
                    binding.buttonLogOut.text = getString(R.string.logout)
                    binding.textViewMoreWishList.visibility = View.VISIBLE
                    binding.recyclerViewWishList.visibility = View.VISIBLE
                    binding.textViewWishList.visibility = View.VISIBLE
                    binding.addressesView.visibility = View.VISIBLE
                    binding.separator3.visibility = View.VISIBLE
                    binding.textView3.visibility = View.VISIBLE
                } else {
                    isLogin = false
                    //binding.buttonLogOut.visibility = View.GONE
                    binding.buttonLogOut.text = getString(R.string.sign_in)
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

    override fun orderClicked(orders: Orders) {
        if(networkConnectivity.isOnline()) {
            val action =
                ProfileFragmentDirections.actionNavigationProfileToOrderDetailsFragment2(orders)
            binding.root.findNavController().navigate(action)
        }else{
            val dialog = MyDialog()
            dialog.showAlertDialog("Please, check your connection", requireContext())
        }
    }

    private fun refresh() {
        if (networkConnectivity.isOnline()) {
            binding.recyclerViewOrders.visibility = View.VISIBLE
            binding.textViewMoreOrders.visibility = View.VISIBLE

            profileViewModel.getOrders()
        } else {
            binding.recyclerViewOrders.visibility = View.GONE
            binding.textViewMoreOrders.visibility = View.GONE
        }

        binding.swipeRefresher.isRefreshing = false
    }

}