package com.mad43.stylista.ui.registration.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentRegistrationBinding
import com.mad43.stylista.ui.registration.viewModel.SignUpViewModel
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.RemoteStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class SignUpFragment : Fragment() {

    private var binding: FragmentRegistrationBinding? = null

    lateinit var registerViewModel: SignUpViewModel
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


        registerViewModel=ViewModelProvider(this)[SignUpViewModel::class.java]


        binding?.buttonSignUp?.setOnClickListener {
            val userName = binding?.editTextUserNameSignUp?.text.toString()
            val email = binding?.editTextEmailSignUp?.text.toString().trim()
            val password = binding?.textPasswordSignUp?.text.toString()
            var confirmPassword = binding?.editTextTextConfirmPasswordSignUp?.text.toString()
                registerViewModel.validateInputs(userName,email, password,confirmPassword)
        }
        binding?.tabSignUp?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.registrationFragment)
        }
        binding?.tabSignIn?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registrationFragment_to_logInFragment)
        }

        lifecycleScope.launch {
            registerViewModel.signUpState.collectLatest {
                when (it) {
                    is RemoteStatus.Success -> {
                        Navigation.findNavController(requireView())
                            .navigate(R.id.logInFragment)
                        dialog.showAlertDialog(getString(R.string.success),requireContext())

                    }
                    is RemoteStatus.Valied -> {
                        dialog.showAlertDialog(getString(it.message), requireContext())
                    }
                    else -> {
                        // Toast.makeText(requireContext(), "Loading", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}