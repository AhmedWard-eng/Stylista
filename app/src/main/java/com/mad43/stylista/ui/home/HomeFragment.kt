package com.mad43.stylista.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.mad43.stylista.databinding.FragmentHomeBinding
import com.denzcoskun.imageslider.models.SlideModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val ads = ArrayList<SlideModel>()

        ads.add(SlideModel("https://picsum.photos/seed/picsum/200/300"))
        ads.add(SlideModel("https://picsum.photos/200/300"))
        ads.add(SlideModel("https://picsum.photos/id/237/200/300"))
        ads.add(SlideModel("https://picsum.photos/200"))
        ads.add(SlideModel("https://picsum.photos/200/300/?blur"))
        ads.add(SlideModel("https://picsum.photos/200/300.jpg"))

        binding.imageSlider.setImageList(ads, ScaleTypes.FIT)
        binding.imageSlider.startSliding(2000)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}