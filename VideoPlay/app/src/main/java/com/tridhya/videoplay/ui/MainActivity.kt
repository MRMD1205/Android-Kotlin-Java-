package com.tridhya.videoplay.ui

import android.os.Bundle
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.ActivityMainBinding
import com.tridhya.videoplay.extentions.invisible
import com.tridhya.videoplay.extentions.visible
import com.tridhya.videoplay.ui.base.BaseActivity

open class MainActivity : BaseActivity(), NavController.OnDestinationChangedListener {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val list = arrayListOf<LinearLayoutCompat>()
    private val selectedPosition: MutableLiveData<Int> = MutableLiveData()
    private var showNav = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.navHostFragment)
        navController.navigate(R.id.fragmentHome)

        navController.addOnDestinationChangedListener(this)

        binding.lifecycleOwner = this
        binding.selectedPosition = selectedPosition.value

        list.clear()
        list.addAll(
            listOf(
                binding.llHome,
                binding.llHeart,
                binding.llCreate,
                binding.llMessage,
                binding.llProfile,
            )
        )
        binding.llHome.isSelected = true

//        showBottomNav(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        navController = findNavController(R.id.navHostFragment)
        return /*navController!!.navigateUp(appBarConfiguration) ||*/ super.onSupportNavigateUp()
    }

    fun showBottomNav(visible: Boolean = true, selectPos: Int = 0) {
        selectedPosition.observeForever {
            changePosition(it)
        }
        if (selectPos != null) {
            selectedPosition.value = selectPos
        }
        if (visible) {
            binding.bottomAppBar.visible()
            binding.llHome.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 0
                navController.navigate(R.id.fragmentHome)
            }
            binding.llHeart.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 1
                navController.navigate(R.id.fragmentfavourites)
            }
            binding.llCreate.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 2
//                onMediaPicked()
                navController.navigate(R.id.fragmentCreateMedia)
            }
            binding.llMessage.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 3
                navController.navigate(R.id.fragmentMessages)
            }
            binding.llProfile.setOnClickListener {
                preventDoubleClick(it)
                selectedPosition.value = 4
                navController.navigate(R.id.fragmentProfile)
            }
        } else {
            binding.bottomAppBar.invisible()
        }
    }

//    private fun onMediaPicked() {
//        try {
//            TedImagePicker
//                .with(this)
//                .mediaType(MediaType.VIDEO)
//                .title("Select Video")
//                .start { uri ->
//                    val size = FileUtils(this).getFileSize(uri)
//                    if (size != null && size <= 30000000) {
//                        videoSelected(uri)
//                    }
//                }
//        } catch (e: Exception) {
//            showToast("Invalid file format")
//        }
//    }
//
//    private fun videoSelected(uri: Uri) {
//        showToast("Video Selected : $uri")
//    }

    private fun changePosition(pos: Int) {
        if (list.isNotEmpty()) {
//            binding.tvMessageCount.isSelected = pos == 1
            for (i in list.indices) {
                list[i].isSelected = pos == i
            }
        }
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?,
    ) {
        when (navController.currentDestination?.id) {
            R.id.fragmentHome -> {
                showNav = true
                showMoreOptions(true)
                showBottomNav(true, 0)
            }
            R.id.fragmentfavourites -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 1)
            }
            R.id.fragmentCreateMedia -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(false, 2)
            }
            R.id.fragmentMessages -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 3)
            }
            R.id.fragmentProfile -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 4)
            }
            R.id.fragmentGalleryMedia -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(false, 4)
            }
            else -> {
                showNav = false
                showBottomNav(false)
            }
        }
    }

    private fun showMoreOptions(visible: Boolean) {
        binding.llSearch.isVisible = visible
        binding.llMoreOptions.isVisible = visible
    }

    override fun onResume() {
        super.onResume()
        when (navController.currentDestination?.id) {
            R.id.fragmentHome -> {
                showNav = true
                showMoreOptions(true)
                showBottomNav(true, 0)
            }
            R.id.fragmentfavourites -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 1)
            }
            R.id.fragmentCreateMedia -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(false, 2)
            }
            R.id.fragmentMessages -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 3)
            }
            R.id.fragmentProfile -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(true, 4)
            }
            R.id.fragmentGalleryMedia -> {
                showNav = true
                showMoreOptions(false)
                showBottomNav(false, 4)
            }
            else -> {
                showNav = false
                showBottomNav(false)
            }
        }
    }

}