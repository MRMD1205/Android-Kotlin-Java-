package com.tridhya.videoplay.ui.view.fragments.video

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.window.layout.WindowMetricsCalculator
import com.daasuu.gpuv.camerarecorder.CameraRecordListener
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorder
import com.daasuu.gpuv.camerarecorder.GPUCameraRecorderBuilder
import com.daasuu.gpuv.camerarecorder.LensFacing
import com.tridhya.videoplay.R
import com.tridhya.videoplay.databinding.DialogLayoutsBinding
import com.tridhya.videoplay.databinding.DialogScheduleLengthBinding
import com.tridhya.videoplay.databinding.FragmentAddVideoBinding
import com.tridhya.videoplay.enums.FilterType
import com.tridhya.videoplay.extentions.gone
import com.tridhya.videoplay.extentions.visible
import com.tridhya.videoplay.ui.base.BaseFragment
import com.tridhya.videoplay.ui.view.adapters.video.FilterTypeAdapter
import com.tridhya.videoplay.widgets.PortraitFrameLayout
import com.tridhya.videoplay.widgets.SampleCameraGLView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class AddVideoFragment : BaseFragment(), View.OnClickListener, FilterTypeAdapter.onClicked {

    private lateinit var binding: FragmentAddVideoBinding
    private var sampleGLView: SampleCameraGLView? = null
    private var gpuCameraRecorder: GPUCameraRecorder? = null
    private var filepath: String? = null
    private var lensFacing = LensFacing.BACK
    private var cameraWidth = 1280
    private var cameraHeight = 720
    private var videoWidth = 720
    private var videoHeight = 720
    private var customVideoWidth = 720
    private var customVideoHeight = 720
    private var toggleClick = false
    private var filterList: List<FilterType> = emptyList()
    private var filterTypeAdapter: FilterTypeAdapter? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 88888
    private var isFilterEnabled = false
    private var isRecording = false
    private var videoLength = 0
    private var selectedLayout = "Default"
    private var isWatermarkDisabled = false
    private var isDefaultLayout = true
    private var frame: PortraitFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddVideoBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val windowMetrics =
            WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(requireActivity())
        val currentBounds = windowMetrics.bounds // E.g. [0 0 1350 1800]
        videoWidth = currentBounds.width()
        videoHeight = currentBounds.height()
        binding.ivRecord.setOnClickListener(this)
        binding.ivSwitchCamera.setOnClickListener(this)
        binding.ivFlash.setOnClickListener(this)
        binding.ivFilter.setOnClickListener(this)
        binding.ivScheduleLength.setOnClickListener(this)
        binding.ivWatermark.setOnClickListener(this)
        binding.ivLayout.setOnClickListener(this)
        binding.ivGallery.setOnClickListener(this)

        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rvFilterList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        filterList = FilterType.createFilterList()
        filterTypeAdapter = FilterTypeAdapter(filterList, this)
        binding.rvFilterList.adapter = filterTypeAdapter
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivRecord -> {
                if (!isRecording) {
                    if (videoLength != 0) {
                        filepath = getVideoFilePath()
                        gpuCameraRecorder!!.start(filepath)
                        showTimer(true)
                        binding.ivRecord.setImageResource(R.drawable.ic_recording_on)
                        val timer = object :
                            CountDownTimer(
                                TimeUnit.SECONDS.toMillis((videoLength + 1).toLong()),
                                1000
                            ) {
                            override fun onTick(millisUntilFinished: Long) {

                            }

                            override fun onFinish() {
                                gpuCameraRecorder!!.stop()
                                showTimer(false)
                                binding.ivRecord.setImageResource(R.drawable.ic_recording_off)
                            }
                        }
                        timer.start()
                    } else {
                        filepath = getVideoFilePath()
                        gpuCameraRecorder!!.start(filepath)
                        showTimer(true)
                        binding.ivRecord.setImageResource(R.drawable.ic_recording_on)
                    }
                } else {
                    gpuCameraRecorder!!.stop()
                    showTimer(false)
                    binding.ivRecord.setImageResource(R.drawable.ic_recording_off)
                }

                isRecording = !isRecording
            }
            R.id.ivSwitchCamera -> {
                if (isDefaultLayout) {
                    releaseCamera(binding.frame)
                } else {
                    releaseCamera(frame!!)
                }
                lensFacing = if (lensFacing == LensFacing.BACK) {
                    LensFacing.FRONT
                } else {
                    LensFacing.BACK
                }
                Handler(Looper.getMainLooper()).postDelayed({
                    setUpCamera()
                    toggleClick = true
                }, 500)
            }
            R.id.ivFlash -> {
                if (gpuCameraRecorder != null && gpuCameraRecorder!!.isFlashSupport) {
                    gpuCameraRecorder!!.switchFlashMode()
                    gpuCameraRecorder!!.changeAutoFocus()
                }
            }
            R.id.ivFilter -> {
                binding.rvFilterList.isVisible = !isFilterEnabled
                isFilterEnabled = !isFilterEnabled
            }
            R.id.ivScheduleLength -> {
                showPopUpMenu(binding.ivScheduleLength)
            }
            R.id.ivWatermark -> {
                if (isDefaultLayout) {
                    showWaterMark(isWatermarkDisabled)
                }
            }
            R.id.ivGallery -> {
                findNavController().navigate(R.id.toGalleryMedia)
            }
            R.id.ivLayout -> {
                showLayouts(binding.ivLayout)
            }
        }
    }

    private fun showLayouts(view: AppCompatImageView) {
        val dialogBinding =
            DialogLayoutsBinding.inflate(LayoutInflater.from(context))
        val popUp = PopupWindow(
            dialogBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popUp.animationStyle = androidx.transition.R.style.Animation_AppCompat_Dialog
        popUp.showAsDropDown(view, 0, 0, Gravity.CENTER)

        dialogBinding.ivDefaultLayout.isSelected = true

        when (selectedLayout) {
            "Default" -> {
                dialogBinding.ivDefaultLayout.isSelected = true
            }
            "3 Part" -> {
                dialogBinding.iv3PartLayout.isSelected = true
            }
        }

        dialogBinding.ivDefaultLayout.setOnClickListener {
            selectedLayout = "Default"
            isDefaultLayout = true
            popUp.dismiss()
            setCustomLayout(false)
            checkPermission()
        }
        dialogBinding.iv3PartLayout.setOnClickListener {
            selectedLayout = "3 Part"
            isDefaultLayout = false
            popUp.dismiss()
            setCustomLayout(true)
            checkPermission()
        }
    }

    private fun setCustomLayout(isCustomLayout: Boolean) {
        if (isCustomLayout) {

            binding.cvFrame1.viewTreeObserver
                .addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                    override fun onGlobalLayout() {
                        // Ensure you call it only once :
                        binding.cvFrame1.viewTreeObserver.removeGlobalOnLayoutListener(this)

                        customVideoWidth = binding.cvFrame1.width
                        customVideoHeight = binding.cvFrame1.height
                    }
                })

            binding.frame.gone()
            binding.ll3PartLayout.visible()
        } else {
            binding.frame.visible()
            binding.ll3PartLayout.gone()
        }
    }

    private fun showWaterMark(watermarkDisabled: Boolean) {
        if (!watermarkDisabled) {
            gpuCameraRecorder!!.setFilter(
                FilterType.createGlFilter(
                    FilterType.WATERMARK,
                    requireContext().applicationContext
                )
            )
            binding.ivWatermark.setImageResource(R.drawable.ic_app)
        } else {
            gpuCameraRecorder!!.setFilter(
                FilterType.createGlFilter(
                    FilterType.DEFAULT,
                    requireContext().applicationContext
                )
            )
            binding.ivWatermark.setImageResource(R.drawable.ic_app_disable)
        }
        isWatermarkDisabled = !isWatermarkDisabled
    }

    private fun showPopUpMenu(view: View) {
        val dialogBinding =
            DialogScheduleLengthBinding.inflate(LayoutInflater.from(context))
        val popUp = PopupWindow(
            dialogBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popUp.animationStyle = androidx.transition.R.style.Animation_AppCompat_Dialog
        popUp.showAsDropDown(view, 0, 0, Gravity.CENTER)

        when (videoLength) {
            0 -> {
                dialogBinding.ivSchedule.isSelected = true
            }
            15 -> {
                dialogBinding.ivSchedule15.isSelected = true
            }
            30 -> {
                dialogBinding.ivSchedule30.isSelected = true
            }
            45 -> {
                dialogBinding.ivSchedule45.isSelected = true
            }
        }

        dialogBinding.ivSchedule.setOnClickListener {
            videoLength = 0
            binding.ivScheduleLength.setImageResource(R.drawable.ic_schedule)
            popUp.dismiss()
        }
        dialogBinding.ivSchedule15.setOnClickListener {
            videoLength = 15
            binding.ivScheduleLength.setImageResource(R.drawable.ic_schedule_15)
            popUp.dismiss()
        }
        dialogBinding.ivSchedule30.setOnClickListener {
            videoLength = 30
            binding.ivScheduleLength.setImageResource(R.drawable.ic_schedule_30)
            popUp.dismiss()
        }
        dialogBinding.ivSchedule45.setOnClickListener {
            videoLength = 45
            binding.ivScheduleLength.setImageResource(R.drawable.ic_schedule_45)
            popUp.dismiss()
        }
    }

    private fun showTimer(visibility: Boolean) {
        if (visibility) {
            binding.tvTimer.visible()
            binding.tvTimer.base = SystemClock.elapsedRealtime()
            binding.tvTimer.start()
        } else {
            binding.tvTimer.stop()
            binding.tvTimer.gone()
        }
    }

    private fun releaseCamera(view: PortraitFrameLayout) {
        sampleGLView?.onPause()
        if (gpuCameraRecorder != null) {
            gpuCameraRecorder!!.stop()
            gpuCameraRecorder!!.release()
            gpuCameraRecorder = null
        }
        if (sampleGLView != null) {
            (view as FrameLayout).removeView(sampleGLView)
            sampleGLView = null
        }
    }

    fun getVideoFilePath(): String {
        return getAndroidMoviesFolder()?.absolutePath + "/" + SimpleDateFormat("yyyyMM_dd-HHmmss").format(
            Date()
        ) + "GPUCameraRecorder.mp4"
    }

    fun getAndroidMoviesFolder(): File? {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES)
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }

    override fun onStop() {
        super.onStop()
        if (isDefaultLayout) {
            releaseCamera(binding.frame)
        } else {
            frame?.let { releaseCamera(it) }
        }
    }

    private fun setUpCamera() {
        if (isDefaultLayout) {
            setUpCameraView(binding.frame)
            gpuCameraRecorder =
                GPUCameraRecorderBuilder(requireActivity(), sampleGLView) //.recordNoFilter(true)
                    .cameraRecordListener(object : CameraRecordListener {
                        override fun onGetFlashSupport(flashSupport: Boolean) {
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    binding.ivFlash.isEnabled = flashSupport
                                }
                            }
                        }

                        override fun onRecordComplete() {
                            exportMp4ToGallery(
                                requireContext().applicationContext,
                                filepath.toString()
                            )
                        }

                        override fun onRecordStart() {
                            Handler(Looper.myLooper()!!).post { binding.rvFilterList.gone() }
                        }

                        override fun onError(exception: Exception) {
                            Log.e("GPUCameraRecorder", exception.toString())
                        }

                        override fun onCameraThreadFinish() {
                            if (toggleClick) {
                                Handler(Looper.myLooper()!!).post { setUpCamera() }
                            }
                            toggleClick = false
                        }

                        override fun onVideoFileReady() {}
                    })
                    .videoSize(videoWidth, videoHeight)
                    .cameraSize(cameraWidth, cameraHeight)
                    .lensFacing(lensFacing)
                    .build()
        } else {
//            if (frame == null) {
//                frame = binding.frame1
//            }
            setUpCameraView(binding.frame1)
            gpuCameraRecorder =
                GPUCameraRecorderBuilder(
                    requireActivity(),
                    sampleGLView
                ) //.recordNoFilter(true)
                    .cameraRecordListener(object : CameraRecordListener {
                        override fun onGetFlashSupport(flashSupport: Boolean) {
                            GlobalScope.launch {
                                withContext(Dispatchers.Main) {
                                    binding.ivFlash.isEnabled = flashSupport
                                }
                            }
                        }

                        override fun onRecordComplete() {
                            exportMp4ToGallery(
                                requireContext().applicationContext,
                                filepath.toString()
                            )
                            frame = binding.frame2
                            setUpCameraView(binding.frame2)
                            setCamera2()
                        }

                        override fun onRecordStart() {
                            Handler(Looper.myLooper()!!).post { binding.rvFilterList.gone() }
                        }

                        override fun onError(exception: Exception) {
                            Log.e("GPUCameraRecorder", exception.toString())
                        }

                        override fun onCameraThreadFinish() {
                            if (toggleClick) {
                                Handler(Looper.myLooper()!!).post { setUpCamera() }
                            }
                            toggleClick = false
                        }

                        override fun onVideoFileReady() {}
                    })
                    .videoSize(customVideoWidth, customVideoHeight)
                    .cameraSize(cameraWidth, cameraHeight)
                    .lensFacing(lensFacing)
                    .build()
        }
    }

    private fun setCamera2() {
        gpuCameraRecorder =
            GPUCameraRecorderBuilder(
                requireActivity(),
                sampleGLView
            ) //.recordNoFilter(true)
                .cameraRecordListener(object : CameraRecordListener {
                    override fun onGetFlashSupport(flashSupport: Boolean) {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                binding.ivFlash.isEnabled = flashSupport
                            }
                        }
                    }

                    override fun onRecordComplete() {
                        exportMp4ToGallery(
                            requireContext().applicationContext,
                            filepath.toString()
                        )
//                        frame = binding.frame3
                        setUpCameraView(binding.frame3)
                        setCamera3()
                    }

                    override fun onRecordStart() {
                        Handler(Looper.myLooper()!!).post { binding.rvFilterList.gone() }
                    }

                    override fun onError(exception: Exception) {
                        Log.e("GPUCameraRecorder", exception.toString())
                    }

                    override fun onCameraThreadFinish() {
                        if (toggleClick) {
                            Handler(Looper.myLooper()!!).post { setUpCamera() }
                        }
                        toggleClick = false
                    }

                    override fun onVideoFileReady() {}
                })
                .videoSize(customVideoWidth, customVideoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build()
    }

    private fun setCamera3() {
        gpuCameraRecorder =
            GPUCameraRecorderBuilder(
                requireActivity(),
                sampleGLView
            ) //.recordNoFilter(true)
                .cameraRecordListener(object : CameraRecordListener {
                    override fun onGetFlashSupport(flashSupport: Boolean) {
                        GlobalScope.launch {
                            withContext(Dispatchers.Main) {
                                binding.ivFlash.isEnabled = flashSupport
                            }
                        }
                    }

                    override fun onRecordComplete() {
                        exportMp4ToGallery(
                            requireContext().applicationContext,
                            filepath.toString()
                        )
//                        setUpCamera()
                    }

                    override fun onRecordStart() {
                        Handler(Looper.myLooper()!!).post { binding.rvFilterList.gone() }
                    }

                    override fun onError(exception: Exception) {
                        Log.e("GPUCameraRecorder", exception.toString())
                    }

                    override fun onCameraThreadFinish() {
                        if (toggleClick) {
                            Handler(Looper.myLooper()!!).post { setUpCamera() }
                        }
                        toggleClick = false
                    }

                    override fun onVideoFileReady() {}
                })
                .videoSize(customVideoWidth, customVideoHeight)
                .cameraSize(cameraWidth, cameraHeight)
                .lensFacing(lensFacing)
                .build()
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

    private fun setUpCameraView(view: PortraitFrameLayout) {
        view.removeAllViews()
        sampleGLView = null
        sampleGLView = SampleCameraGLView(requireContext().applicationContext)
        sampleGLView!!.setTouchListener { event, width, height ->
            if (gpuCameraRecorder == null) return@setTouchListener
            gpuCameraRecorder!!.changeManualFocusPoint(
                event.x,
                event.y,
                width,
                height
            )
        }
        view.addView(sampleGLView)
    }

    private fun checkPermission() {
        // request camera permission if it has not been grunted.
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                ), CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            setUpCamera()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_REQUEST_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpCamera()
            } else {
                Toast.makeText(
                    requireContext(),
                    "[WARN] permission is not granted.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun filterSelected(filterType: FilterType) {
        if (gpuCameraRecorder != null) {
            gpuCameraRecorder!!.setFilter(
                FilterType.createGlFilter(
                    filterType,
                    requireContext().applicationContext
                )
            )
        }
    }
}