package com.mad43.stylista.ui.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mad43.stylista.databinding.FragmentLogInBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.ui.login.viewModel.LoginViewModel
import com.mad43.stylista.ui.login.viewModel.LoginViewModelFactory
import com.mad43.stylista.ui.profile.viewModel.ProfileFactoryViewModel
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel

import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetwarkInternet
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!

    lateinit var signInViewModel: LoginViewModel

    var dialog = MyDialog()
    lateinit var favFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLogInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)

        val AuthRepo = AuthRepositoryImp()
        favFactory =
            LoginViewModelFactory(AuthUseCase(AuthRepo), FavouriteLocal(favouriteLocalRepo))
        signInViewModel = ViewModelProvider(this, favFactory).get(LoginViewModel::class.java)
        binding.progressBarSignIn.visibility = View.GONE

        signInViewModel.signInStateLiveData.observe(viewLifecycleOwner) {
            val navController = Navigation.findNavController(view)
            when (it) {
                is RemoteStatus.Success -> {
                    navController.navigate(R.id.navigation_home)
                    signInViewModel.insertAll()
                    binding.progressBarSignIn.visibility = View.GONE

                }

                is RemoteStatus.Valied -> {
                    dialog.showAlertDialog(getString(it.message), requireContext())
                    binding.progressBarSignIn.visibility = View.GONE
                }

                is RemoteStatus.Failure -> {
                    dialog.showAlertDialog("Fail", requireContext())
                    binding.progressBarSignIn.visibility = View.GONE
                }

                is RemoteStatus.Loading -> {

                }

                else -> {
                    binding.progressBarSignIn.visibility = View.GONE
                }
            }
        }


        binding.buttonSignIn.setOnClickListener {
            if (NetwarkInternet().isNetworkAvailable(requireContext())) {
                binding.progressBarSignIn.visibility = View.VISIBLE
                val email = binding.editTextEmailSignIn.text.toString()
                val password = binding.textPasswordSignIn.text.toString()
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
        binding.tabSignIn.setOnClickListener {
            navController
                .navigate(R.id.logInFragment)
        }
        binding.textViewSkip.setOnClickListener {
            navController.navigate(R.id.action_logInFragment_to_navigation_home)
        }
        signInViewModel.checkUserIsLogin()


        lifecycleScope.launch {
            val navController = Navigation.findNavController(view)
            signInViewModel.userExists.collect { userExists ->
                if (userExists) {
                    navController.navigate(R.id.navigation_home)
                } else {
                    // dialog.showAlertDialog(getString(R.string.check_login), requireContext())
                }
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}