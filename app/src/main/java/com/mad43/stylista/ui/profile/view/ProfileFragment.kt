package com.mad43.stylista.ui.profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.mad43.stylista.R
import com.mad43.stylista.data.local.db.ConcreteLocalSource
import com.mad43.stylista.data.repo.auth.AuthRepositoryImp
import com.mad43.stylista.data.repo.favourite.FavouriteLocalRepoImp
import com.mad43.stylista.databinding.FragmentProfileBinding
import com.mad43.stylista.domain.local.favourite.FavouriteLocal
import com.mad43.stylista.domain.remote.auth.AuthUseCase
import com.mad43.stylista.domain.remote.productDetails.ProductInfo
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModel
import com.mad43.stylista.ui.productInfo.viewModel.ProductInfoViewModelFactory
import com.mad43.stylista.ui.profile.viewModel.ProfileFactoryViewModel
import com.mad43.stylista.ui.profile.viewModel.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    lateinit var profileViewModel : ProfileViewModel
    lateinit var favFactory: ProfileFactoryViewModel

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

      //  profileViewModel=ViewModelProvider(this)[ProfileViewModel::class.java]

        val context = requireContext().applicationContext
        val localSource = ConcreteLocalSource(context)
        val favouriteLocalRepo = FavouriteLocalRepoImp(localSource)

        val AuthRepo = AuthRepositoryImp()
        favFactory = ProfileFactoryViewModel(AuthUseCase(AuthRepo), FavouriteLocal(favouriteLocalRepo))
        profileViewModel = ViewModelProvider(this, favFactory).get(ProfileViewModel::class.java)


        var userName = profileViewModel.getUserName()
        binding.textViewHelloUserName.text = "Welcom ${userName}"

        binding.buttonLogOut.setOnClickListener {
            profileViewModel.logout()
            Navigation.findNavController(requireView())
                .navigate(R.id.action_navigation_profile_to_logInFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}