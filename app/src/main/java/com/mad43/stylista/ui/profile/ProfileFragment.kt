package com.mad43.stylista.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mad43.stylista.R
import com.mad43.stylista.databinding.FragmentProfileBinding
import com.mad43.stylista.ui.login.viewModel.LoginViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var profileViewModel : ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel=ViewModelProvider(this)[ProfileViewModel::class.java]

        var userName = profileViewModel.getUserName()
        binding.textViewHelloUserName.text = "Welcome $userName"

        binding.buttonLogOut.setOnClickListener {
            profileViewModel.logout()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_logInFragment)
        }

        binding.addressesView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_addressListFragment)
        }

        binding.textViewCurrencyCode.text = profileViewModel.getCurrencyCode()
        binding.currencyView.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_currencyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}