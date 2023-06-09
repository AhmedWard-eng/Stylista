package com.mad43.stylista.ui.registration.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.mad43.stylista.R
import com.mad43.stylista.data.repo.auth.SignUpRepositoryImpl
import com.mad43.stylista.databinding.FragmentRegistrationBinding
import com.mad43.stylista.domain.remote.auth.SignUpUseCase
import com.mad43.stylista.ui.registration.viewModel.SignUpState
import com.mad43.stylista.ui.registration.viewModel.SignUpViewModel
import com.mad43.stylista.ui.registration.viewModel.SignUpViewModelFactory
import com.mad43.stylista.util.MyDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    private lateinit var authViewModel: SignUpViewModel
    lateinit var allFactory: SignUpViewModelFactory
    var dialog =MyDialog()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allFactory= SignUpViewModelFactory(SignUpUseCase(SignUpRepositoryImpl(FirebaseAuth.getInstance())))
        authViewModel= ViewModelProvider(this,allFactory).get(SignUpViewModel::class.java)


        binding?.buttonSignUp?.setOnClickListener {

            val email = binding?.editTextEmailSignUp?.text.toString().trim()
            val password = binding?.textPasswordSignUp?.text.toString()
            var confirmPassword = binding?.editTextTextConfirmPasswordSignUp?.text.toString()
                authViewModel.validateInputs(email, password,confirmPassword)
               // dialog.showAlertDialog("check your email to verified:)",requireContext())
                observeData()
                observeErrorMessage()
        }
        binding?.tabSignUp?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registrationFragment_self)
        }
        binding?.tabSignIn?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registrationFragment_to_logInFragment)
        }

    }

    private fun observeData() {
        lifecycleScope.launch  {
            authViewModel.validationStateFlow.collectLatest {
                when (it) {
                    is SignUpState.onSuccess -> {
                        dialog.showAlertDialog(getString(it.message),requireContext())
                        Navigation.findNavController(requireView())
                            .navigate(R.id.action_registrationFragment_to_logInFragment)
                    }
                    is SignUpState.onError -> {
                        dialog.showAlertDialog(getString(it.message),requireContext())
                    }
                    else -> {

                    }
                }
            }
        }
    }


    private fun observeErrorMessage() {
        lifecycleScope.launch  {
            authViewModel.errorStateFlow.collectLatest { it ->
                dialog.showAlertDialog(it?.message ?: "",requireContext())
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}