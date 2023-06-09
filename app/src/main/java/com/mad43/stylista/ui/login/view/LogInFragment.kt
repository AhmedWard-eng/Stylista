package com.mad43.stylista.ui.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mad43.stylista.R
import com.mad43.stylista.data.remote.network.ApiService
import com.mad43.stylista.data.repo.auth.LoginRepositoryImp
import com.mad43.stylista.data.repo.auth.SignUpRepository
import com.mad43.stylista.data.repo.auth.SignUpRepositoryImpl
import com.mad43.stylista.databinding.FragmentLogInBinding
import com.mad43.stylista.domain.remote.auth.LoginUseCase
import com.mad43.stylista.ui.login.viewModel.LoginState
import com.mad43.stylista.ui.login.viewModel.LoginViewModel
import com.mad43.stylista.ui.login.viewModel.LoginViewModelFactory
import com.mad43.stylista.util.MyDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LogInFragment : Fragment() {

    private var _binding: FragmentLogInBinding? = null
    private val binding get() = _binding!!


    private lateinit var loginViewModel: LoginViewModel
    lateinit var allFactory: LoginViewModelFactory
    var dialog = MyDialog()

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


        val signUpRepository = SignUpRepositoryImpl(FirebaseAuth.getInstance())

        allFactory = LoginViewModelFactory(
            LoginUseCase(
                LoginRepositoryImp(ApiService.APIClient.loginAPIService),
                signUpRepository
            )
        )
        loginViewModel = ViewModelProvider(this, allFactory).get(LoginViewModel::class.java)

        binding.buttonSignIn.setOnClickListener {
            val email = binding.editTextEmailSignIn.text.toString()
            val password = binding.textPasswordSignIn.text.toString()
            if (email.isEmpty()) {
                dialog.showAlertDialog(getString(R.string.login_empty_email), requireContext())
            } else if (password.isEmpty()) {
                dialog.showAlertDialog(getString(R.string.login_empty_password), requireContext())
            } else {
                loginViewModel.login(email, password)
            }
        }
        binding?.tabSignUp?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_logInFragment_to_registrationFragment)
        }
        binding?.tabSignIn?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_logInFragment_self)
        }
        binding?.textViewSkip?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_logInFragment_to_navigation_home)
        }


        lifecycleScope.launch {
            loginViewModel.loginState.collectLatest {
                when (it) {
                    is LoginState.Success -> {
                        view.findNavController()
                            .navigate(R.id.action_logInFragment_to_navigation_home)
                    }
                    is LoginState.Failed -> {
                        dialog.showAlertDialog(getString(it.message), requireContext())
                    }
                    else -> {
                        Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

//        loginViewModel.checkUserIsLogin()
//
//        lifecycleScope.launch {
//            loginViewModel.userExists.collect { userExists ->
//                if (userExists) {
//                    view.findNavController().navigate(R.id.action_logInFragment_to_navigation_home)
//                } else {
//                    dialog.showAlertDialog(getString(R.string.check_login), requireContext())
//                }
//            }
//        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}