package com.tridhya.videoplay.ui.view.fragments.favourites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.FragmentFavouritesBinding
import com.tridhya.videoplay.databinding.FragmentHomeBinding
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.ui.viewModel.favourites.FavouritesViewModel
import com.tridhya.videoplay.ui.viewModel.home.HomeViewModel

class FavouritesFragment : BaseFragment(), View.OnClickListener {
    private val viewModel by lazy { FavouritesViewModel(requireContext()) }
    private lateinit var binding: FragmentFavouritesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavouritesBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onClick(view: View?) {
        when (view?.id) {

        }
    }
}