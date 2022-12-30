package com.tridhya.videoplay.ui.view.fragments.video

import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import androidx.window.layout.WindowMetricsCalculator
import com.daasuu.gpuv.composer.FillMode
import com.daasuu.gpuv.composer.GPUMp4Composer
import com.daasuu.gpuv.egl.filter.GlFilter
import com.daasuu.gpuv.egl.filter.GlFilterGroup
import com.daasuu.gpuv.egl.filter.GlMonochromeFilter
import com.daasuu.gpuv.egl.filter.GlVignetteFilter
import com.daasuu.gpuv.player.GPUPlayerView
import com.daasuu.gpuv.player.PlayerScaleType
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.FragmentGalleryVideoBinding
import com.tridhya.videoplay.dialogs.FiltersBottomDialog
import com.tridhya.videoplay.enums.FilterType
import com.tridhya.videoplay.model.VideoItem
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.utils.FilePathUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class GalleryVideoFragment : BaseFragment(), View.OnClickListener,
    FiltersBottomDialog.FilterSelectedListener {

    private lateinit var binding: FragmentGalleryVideoBinding
    private var player: SimpleExoPlayer? = null
    private var gpuPlayerView: GPUPlayerView? = null
    private var videoUri: Uri? = null
    private var videoPath: String? = null
    private var progressDialog: ProgressDialog? = null
    private var GPUMp4Composer: GPUMp4Composer? = null
    var selectedFilterType: FilterType? = null
    private var glFilter: GlFilter? = GlFilterGroup(GlMonochromeFilter(), GlVignetteFilter())
    private var videoItem: VideoItem? = null

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                videoUri = data?.data
                Log.e("Video path : ", videoUri?.path.toString())
                initPlayer(videoUri)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryVideoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog = ProgressDialog(requireContext())

        pickVideo()

        binding.btnClose.setOnClickListener(this)
        binding.btnSave.setOnClickListener(this)
        binding.ivFilter.setOnClickListener(this)
    }

    private fun initPlayer(videoUri: Uri?) {
        player = SimpleExoPlayer.Builder(requireContext())
            .setTrackSelector(DefaultTrackSelector(requireContext()))
            .build()

        player?.addMediaItem(MediaItem.fromUri(videoUri.toString()))
        player?.prepare()
        player!!.playWhenReady = true

        player?.addListener(object : Player.Listener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    player?.seekTo(0)
                    player?.playWhenReady = true
                }
            }
        })

        setUpGpuPlayerView()
    }

    private fun setUpGpuPlayerView() {
        gpuPlayerView = GPUPlayerView(requireContext())
        gpuPlayerView?.setSimpleExoPlayer(player)
        gpuPlayerView?.setPlayerScaleType(PlayerScaleType.RESIZE_NONE)
        gpuPlayerView?.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        binding.layoutMovieWrapper.addView(gpuPlayerView)
        gpuPlayerView?.onResume()
    }

    private fun pickVideo() {
        resultLauncher.launch(
            Intent.createChooser(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                ), "Select Video"
            )
        )
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btnClose -> {
                findNavController().navigateUp()
            }

            R.id.btnSave -> {
                saveFilteredVideo()
            }

            R.id.ivFilter -> {
                activity?.supportFragmentManager?.let {
                    FiltersBottomDialog.newInstance(
                        FilterType.createFilterList(),
                        this
                    ).show(
                        it, "Filter Dialog"
                    )
                }
            }
        }
    }

    fun getAndroidMoviesFolder(): File? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    }

    fun getVideoFilePath(): String? {
        return getAndroidMoviesFolder()?.absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(
            Date()
        ) + "filter_apply.mp4"
    }

    fun exportMp4ToGallery(context: Context, filePath: String) {
        val values = ContentValues(2)
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4")
        values.put(MediaStore.Video.Media.DATA, filePath)
        context.contentResolver.insert(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            values
        )
        context.sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://$filePath")
            )
        )
    }

    private fun saveFilteredVideo() {
        val windowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
        glFilter = null
        glFilter =
            FilterType.createGlFilter(selectedFilterType, requireContext().applicationContext)
        videoPath = getVideoFilePath()
        progressDialog?.setTitle("Saving Video")
        progressDialog?.setMessage("Saving... 0")
        progressDialog?.show()
        GPUMp4Composer = null
        GPUMp4Composer =
            GPUMp4Composer(
                FilePathUtil.getPath(requireContext(), videoUri!!),
                videoPath
            ) // .rotation(Rotation.ROTATION_270)
                .size(currentBounds.width(), currentBounds.height())
                .fillMode(FillMode.PRESERVE_ASPECT_FIT)
                .filter(glFilter)
                .mute(false)
                .flipHorizontal(false)
                .flipVertical(false)
                .listener(object : GPUMp4Composer.Listener {
                    override fun onProgress(progress: Double) {
                        Log.d(
                            "SAVING PROGRESS",
                            "onProgress = $progress"
                        )
                        Handler(Looper.getMainLooper()).post {
                            progressDialog?.setMessage("Saving... " + (progress * 100).toInt())
                        }
                    }

                    override fun onCompleted() {
                        Log.d(
                            "SAVING STATUS",
                            "onCompleted()"
                        )
                        exportMp4ToGallery(
                            requireContext().applicationContext,
                            videoPath.toString()
                        )
                        Handler(Looper.getMainLooper()).post {
                            progressDialog?.hide()
//                            findViewById<View>(R.id.start_codec_button).setEnabled(true)
//                            findViewById<View>(R.id.start_play_movie).setEnabled(true)
                            Toast.makeText(
                                requireContext(),
                                "codec complete path =$videoPath",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCanceled() {}
                    override fun onFailed(exception: Exception) {
                        Log.d("SAVING STATUS", "onFailed()")
                    }
                })
                .start()
    }

    private fun releasePlayer() {
        gpuPlayerView?.onPause()
        binding.layoutMovieWrapper.removeAllViews()
        gpuPlayerView = null
        player?.stop()
        player?.release()
        player = null
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
    }

    override fun onResume() {
        super.onResume()
        if (videoUri != null)
            initPlayer(videoUri)
    }

    override fun onFilterSelected(filterType: FilterType) {
        selectedFilterType = filterType
        val filter = FilterType.createGlFilter(
            filterType,
            requireContext().applicationContext
        )
        gpuPlayerView!!.setGlFilter(filter)
    }

}