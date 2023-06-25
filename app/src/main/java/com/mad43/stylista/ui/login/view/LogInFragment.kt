package com.mad43.stylista.ui.login.view

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.*
import com.mad43.stylista.databinding.FragmentLogInBinding
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.local.entity.Favourite
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.login.viewModel.DraftOrderState
import com.mad43.stylista.ui.login.viewModel.LoginViewModel
import com.mad43.stylista.ui.login.viewModel.LoginViewModelFactory
import com.mad43.stylista.ui.productInfo.model.ApiState
import com.mad43.stylista.ui.profile.viewModel.ProfileFactoryViewModel
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel

import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    lateinit var signInViewModel: LoginViewModel

    var dialog = MyDialog()
    lateinit var favFactory: LoginViewModelFactory



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override  fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)

        val AuthRepo = AuthRepositoryImp()
        favFactory =
            LoginViewModelFactory(AuthUseCase(AuthRepo), FavouriteLocal(favouriteLocalRepo))
        signInViewModel = ViewModelProvider(this, favFactory).get(LoginViewModel::class.java)
        binding?.progressBarSignIn?.visibility = View.GONE

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                signInViewModel.loginState.collect {

                        uiState ->when (uiState) {
                    is RemoteStatus.Success -> {
                        getDraftOrder()
                        observeDraftOrder()
                        binding.progressBarSignIn.visibility = View.GONE
                    }
                    is RemoteStatus.Valied -> {
                        dialog.showAlertDialog(getString(uiState.message), requireContext())
                        binding.progressBarSignIn.visibility = View.GONE
                        signInViewModel.returnReload()
                    }
                    is RemoteStatus.Failure ->{
                        dialog.showAlertDialog("Fail", requireContext())
                        binding.progressBarSignIn.visibility = View.GONE
                    }
                    else ->{
                        binding.progressBarSignIn.visibility = View.GONE
                    }
                }

                }
            }

        }

        binding.buttonSignIn.setOnClickListener {
            if (NetwarkInternet().isNetworkAvailable(requireContext())) {
                binding.progressBarSignIn.visibility = View.VISIBLE
                val email = binding.editTextEmailSignIn.text.toString().trim()
                val password = binding.textPasswordSignIn.text.toString().trim()
                if (email.isEmpty()) {
                    dialog.showAlertDialog(getString(R.string.login_empty_email), requireContext())
                } else if (password.isEmpty()) {
                    dialog.showAlertDialog(
                        getString(R.string.login_empty_password),
                        requireContext()
                    )
                } else {
                    signInViewModel.login(email, password)
                }
            } else {
                NetwarkInternet().displayNetworkDialog(requireContext())
            }

        }
        val navController = Navigation.findNavController(view)
        binding.tabSignUp.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_registrationFragment)
        }
        binding.textViewSkip.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_navigation_home)
        }
        binding.imageView3.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_navigation_home)
        }
        signInViewModel.checkUserIsLogin()
        lifecycleScope.launch {
            val navController = Navigation.findNavController(view)
            signInViewModel.userExists.collect { userExists ->
                if (userExists) {
                    navController.navigate(R.id.action_logInFragment_to_navigation_home)
                } else {
                    // dialog.showAlertDialog(getString(R.string.check_login), requireContext())
                }
            }
        }

    }

    fun getDraftOrder(){
        try{
            var favID = signInViewModel.getIDForFavourite()
            signInViewModel.getDraftOrder(favID.toString())
        }catch (e : Exception){
        }

    }
    fun observeDraftOrder(){
        val navController = view?.let { Navigation.findNavController(it) }
        lifecycleScope.launchWhenStarted{
            signInViewModel.draftOrder.collect{
                when(it){
                    is DraftOrderState.OnSuccess ->{
                        val data = it.draftOrder
                        if (data != null && data.line_items != null) {
                            for (lineItem in data.line_items) {
                                var properties = lineItem.properties
                                val urlImage = properties?.find { it.name == "url_image" }?.value
                                if (lineItem.title!= null && lineItem.price!=null && lineItem.product_id!=null&& urlImage!=null && lineItem.variant_id!=null){
                                    var favouriteProduct = Favourite(lineItem.product_id,lineItem.title,lineItem.price, image = urlImage,lineItem.variant_id)
                                    signInViewModel.insertDraftOrder(favouriteProduct)

                                }
                            }
                        }
                        navController?.navigate(R.id.action_logInFragment_to_navigation_home)
                    }
                    is DraftOrderState.OnFail ->{
                    }
                    else ->{

                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}