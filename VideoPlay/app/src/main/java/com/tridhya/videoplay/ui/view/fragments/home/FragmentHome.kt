package com.tridhya.videoplay.ui.view.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.SnapHelper
import com.tridhya.videoplay.databinding.FragmentHomeBinding
import com.tridhya.videoplay.model.MediaModel
import com.tridhya.videoplay.model.User
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.ui.view.adapters.home.VideoAdapter
import com.tridhya.videoplay.ui.viewModel.home.HomeViewModel

class FragmentHome : BaseFragment(), View.OnClickListener {
    private val viewModel by lazy { HomeViewModel(requireContext()) }
    private lateinit var binding: FragmentHomeBinding
    private var mediaModelList: ArrayList<MediaModel> = arrayListOf()
    private var userList: List<User> = emptyList()
    private var videoAdapter: VideoAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(
            inflater,
            container,
            false
        )
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchMedia()
//        fetchUsers()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        binding.rvVideoItem.layoutManager = LinearLayoutManager(requireContext())
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(binding.rvVideoItem)
//        binding.rvVideoItem.setMediaModels(mediaModelList)
        videoAdapter = VideoAdapter(mediaModelList)
        binding.rvVideoItem.adapter = videoAdapter

    }

    private fun fetchUsers() {
        userList =
            listOf(
                User(id = "1", name = "User 1", image = "https://picsum.photos/id/237/200/300"),
                User(
                    id = "2",
                    name = "User 2",
                    image = "https://picsum.photos/seed/picsum/200/300"
                ),
                User(id = "3", name = "User 3", image = "https://picsum.photos/200/300?grayscale"),
                User(id = "4", name = "User 4", image = "https://picsum.photos/200/300/?blur=1"),
                User(
                    id = "5",
                    name = "User 5",
                    image = "https://picsum.photos/id/870/200/300?grayscale&blur=1"
                )
            )
    }

    private fun fetchMedia() {
        mediaModelList = arrayListOf(
            MediaModel(
                userId = "1",
                userName = "User 1",
                userProfileImage = "https://picsum.photos/id/237/200/300",
                title = "Video 1",
                likes = 1,
                comments = 1,
                videoUrl =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                description = "I tend to shy away from restaurant chains, but wherever I go, PF Chang&apos;s has solidly good food and, like Starbucks, they&apos;re reliable. We were staying in Boston for a week and after a long day and blah blah blah blah",
                musicTitle = "Music 1 - Music 1 -Music 1 -Music 1 -Music 1 -Music 1 -Music 1 -Music 1 -Music 1 - "
            ),
            MediaModel(
                userId = "2",
                userName = "User 2",
                userProfileImage = "https://picsum.photos/seed/picsum/200/300",
                title = "Video 2",
                likes = 2,
                comments = 2,
                videoUrl =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                description = "Here is the Second video....",
                musicTitle = "Music 2"
            ),
            MediaModel(
                userId = "3",
                userName = "User 3",
                userProfileImage = "https://picsum.photos/200/300?grayscale",
                title = "Video 3",
                likes = 3,
                comments = 3,
                videoUrl =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                description = "Here is the three video....",
                musicTitle = "Music 3"
            ),
            MediaModel(
                userId = "4",
                userName = "User 4",
                userProfileImage = "https://picsum.photos/200/300/?blur=1",
                title = "Video 4",
                likes = 4,
                comments = 4,
                videoUrl =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4",
                description = "Here is the fourth video....",
                musicTitle = "Music 4"
            ),
            MediaModel(
                userId = "5",
                userName = "User 5",
                userProfileImage = "https://picsum.photos/id/870/200/300?grayscale&blur=1",
                title = "Video 5",
                likes = 5,
                comments = 5,
                videoUrl =
                "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
                description = "Here is the fifth video....",
                musicTitle = "Music 5"
            ),
        )
    }


    override fun onClick(view: View?) {
        when (view?.id) {

        }
    }

    override fun onResume() {
        super.onResume()
        videoAdapter?.pausePlayers()
    }

    override fun onStop() {
        super.onStop()
        videoAdapter?.stopPlayers()
    }

    override fun onPause() {
        super.onPause()
        videoAdapter?.stopPlayers()
    }
}