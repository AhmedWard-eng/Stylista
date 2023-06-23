package com.mad43.stylista.ui.registration.view

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
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentRegistrationBinding
import com.mad43.stylista.ui.registration.viewModel.SignUpViewModel
import com.mad43.stylista.util.MyDialog
import com.mad43.stylista.util.NetwarkInternet
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
            if(NetwarkInternet().isNetworkAvailable(requireContext())){
                binding?.progressBarSignUp?.visibility = View.VISIBLE
                registerViewModel.userName = binding?.editTextUserNameSignUp?.text.toString().trim()
                registerViewModel.email = binding?.editTextEmailSignUp?.text.toString().trim()
                registerViewModel.password = binding?.textPasswordSignUp?.text.toString().trim()
                registerViewModel.confirmPassword = binding?.editTextTextConfirmPasswordSignUp?.text.toString().trim()
                registerViewModel.validateInputs(registerViewModel.userName,registerViewModel.email, registerViewModel.password,registerViewModel.confirmPassword)
            }else{
                NetwarkInternet().displayNetworkDialog(requireContext())
            }

        }

        binding?.progressBarSignUp?.visibility = View.GONE

        registerViewModel.signUpStateLiveData.observe(viewLifecycleOwner){

            when (it) {
                        is RemoteStatus.Success -> {
                            Navigation.findNavController(requireView())
                                .navigate(R.id.logInFragment)
                            dialog.showAlertDialog(getString(R.string.success),requireContext())
                            binding?.progressBarSignUp?.visibility =View.GONE
                            Log.d(TAG, "Successsssssssssssssssssssssss onViewCreated: ${it}")
                        }
                is RemoteStatus.Valied -> {
                            Log.d(TAG, "dddddeeeeebug onViewCreated: ${it.message}")
                            println("dddddeeeeebug onViewCreated: ${it.message}")
                            dialog.showAlertDialog(getString(it.message), requireContext())
                            binding?.progressBarSignUp?.visibility =View.GONE
                        }
                is RemoteStatus.Loading ->{

                }
                else->{
                    binding?.progressBarSignUp?.visibility =View.GONE
                    Log.d(TAG, "elseeeeeonViewCreated: ${it.toString()}")
                }
            }
        }

        binding?.tabSignIn?.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_registrationFragment_to_logInFragment)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}