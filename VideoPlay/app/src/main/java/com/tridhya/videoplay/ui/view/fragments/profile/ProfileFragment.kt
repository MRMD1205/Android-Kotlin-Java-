package com.tridhya.videoplay.ui.view.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.videoplay.databinding.FragmentProfileBinding
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.ui.viewModel.home.HomeViewModel
import com.tridhya.videoplay.ui.viewModel.profile.ProfileViewModel

class ProfileFragment : BaseFragment(), View.OnClickListener {
    private val viewModel by lazy { ProfileViewModel(requireContext()) }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(
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