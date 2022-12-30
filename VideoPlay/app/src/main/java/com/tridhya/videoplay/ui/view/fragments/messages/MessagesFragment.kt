package com.tridhya.videoplay.ui.view.fragments.messages

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.FragmentFavouritesBinding
import com.tridhya.videoplay.databinding.FragmentMessagesBinding
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.ui.viewModel.home.HomeViewModel
import com.tridhya.videoplay.ui.viewModel.messages.MessagesViewModel

class MessagesFragment : BaseFragment(), View.OnClickListener {
    private val viewModel by lazy { MessagesViewModel(requireContext()) }
    private lateinit var binding: FragmentMessagesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMessagesBinding.inflate(
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